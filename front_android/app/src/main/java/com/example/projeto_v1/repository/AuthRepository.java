package com.example.projeto_v1.repository;


import androidx.lifecycle.MutableLiveData;


import com.example.projeto_v1.model.LoginRequest;

import com.example.projeto_v1.model.LoginResponse;

import com.example.projeto_v1.model.RegisterRequest;

import com.example.projeto_v1.model.UserResponse;

import com.example.projeto_v1.network.ApiService;

import com.example.projeto_v1.network.RetrofitClient;


import retrofit2.Call;

import retrofit2.Callback;

import retrofit2.Response;


public class AuthRepository {


    private final ApiService apiService;


    public AuthRepository() {

        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

    }


    public MutableLiveData<LoginResponse> login(String email, String password) {

        MutableLiveData<LoginResponse> loginData = new MutableLiveData<>();

        LoginRequest request = new LoginRequest(email, password);


        apiService.login(request).enqueue(new Callback<LoginResponse>() {

            @Override

            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    loginData.setValue(response.body());

                } else {

// Aqui poderia tratar erros específicos (401, 400, etc)

                    loginData.setValue(null);

                }

            }


            @Override

            public void onFailure(Call<LoginResponse> call, Throwable t) {

                loginData.setValue(null);

            }

        });


        return loginData;

    }


    public MutableLiveData<UserResponse> register(String name, String email, String password) {

        MutableLiveData<UserResponse> registerData = new MutableLiveData<>();

        RegisterRequest request = new RegisterRequest(name, email, password);


        apiService.register(request).enqueue(new Callback<UserResponse>() {

            @Override

            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    registerData.setValue(response.body());

                } else {

                    registerData.setValue(null);

                }

            }


            @Override

            public void onFailure(Call<UserResponse> call, Throwable t) {

                registerData.setValue(null);

            }

        });


        return registerData;

    }

}