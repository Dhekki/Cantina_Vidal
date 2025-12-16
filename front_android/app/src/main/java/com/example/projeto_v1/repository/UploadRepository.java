package com.example.projeto_v1.repository;



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



        // Criar RequestBody (define que é uma imagem)

        // Se quiser ser genérico, use MediaType.parse("multipart/form-data"), mas para imagem image/* é melhor

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);



        // Criar MultipartBody.Part com o nome do parâmetro que o Controller espera: "file"

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);



        apiService.uploadImage(auth, body).enqueue(new Callback<UploadResponse>() {

            @Override

            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    uploadData.setValue(response.body());

                } else {

                    uploadData.setValue(null);

                }

            }



            @Override

            public void onFailure(Call<UploadResponse> call, Throwable t) {

                uploadData.setValue(null);

            }

        });



        return uploadData;

    }

}