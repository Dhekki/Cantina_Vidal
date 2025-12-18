package com.example.projeto_v1.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.projeto_v1.model.UploadResponse;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadRepository {

    private final ApiService apiService;

    public UploadRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public MutableLiveData<UploadResponse> uploadImage(String token, File file) {
        MutableLiveData<UploadResponse> uploadData = new MutableLiveData<>();
        String auth = (token != null && !token.startsWith("Bearer ")) ? "Bearer " + token : token;

        // Tenta adivinhar o tipo (imagem) ou usa padrao
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        apiService.uploadImage(auth, body).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("UPLOAD_DEBUG", "Sucesso! Caminho: " + response.body().getRelativePath());
                    uploadData.setValue(response.body());
                } else {
                    // --- AQUI ESTÁ A MUDANÇA: Logar o erro ---
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        Log.e("UPLOAD_DEBUG", "Erro no upload. Código: " + response.code() + " Msg: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    uploadData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.e("UPLOAD_DEBUG", "Falha na conexão: " + t.getMessage());
                uploadData.setValue(null);
            }
        });

        return uploadData;
    }
}