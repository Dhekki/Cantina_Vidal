package com.example.projeto_v1.repository;


import androidx.lifecycle.MutableLiveData;


import com.example.projeto_v1.model.UserPatchRequest;
import com.example.projeto_v1.model.UserResponse;

import com.example.projeto_v1.network.ApiService;

import com.example.projeto_v1.network.RetrofitClient;


import retrofit2.Call;

import retrofit2.Callback;

import retrofit2.Response;


public class UserRepository {


    private final ApiService apiService;


    public UserRepository() {

        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

    }


// Método para buscar o perfil do usuário logado

    // O token deve vir com o prefixo "Bearer "

    public MutableLiveData<UserResponse> getMyProfile(String token) {

        MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();


        apiService.getMyProfile(token).enqueue(new Callback<UserResponse>() {

            @Override

            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    userLiveData.setValue(response.body());

                } else {

// Tratar erro (ex: token expirado - 403/401)

                    userLiveData.setValue(null);

                }

            }


            @Override

            public void onFailure(Call<UserResponse> call, Throwable t) {

// Falha na conexão

                userLiveData.setValue(null);

            }

        });


        return userLiveData;

    }

    public MutableLiveData<UserResponse> updateMyProfile(String token, String name, String imageUrl) {
        MutableLiveData<UserResponse> updateLiveData = new MutableLiveData<>();
        String auth = (token != null && !token.startsWith("Bearer ")) ? "Bearer " + token : token;

        UserPatchRequest request = new UserPatchRequest(name, imageUrl);

        apiService.updateMyProfile(auth, request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateLiveData.setValue(response.body());
                } else {
                    // Aqui você pode logar o erro: response.code()
                    updateLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                updateLiveData.setValue(null);
            }
        });

        return updateLiveData;
    }

}