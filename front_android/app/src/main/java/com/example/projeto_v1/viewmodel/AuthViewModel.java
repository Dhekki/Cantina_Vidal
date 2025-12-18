package com.example.projeto_v1.viewmodel;


import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;


import com.example.projeto_v1.model.LoginResponse;

import com.example.projeto_v1.model.UserResponse;

import com.example.projeto_v1.repository.AuthRepository;


public class AuthViewModel extends ViewModel {


    private final AuthRepository repository;


    public AuthViewModel() {

        this.repository = new AuthRepository();

    }


    public LiveData<LoginResponse> fazerLogin(String email, String password) {

        return repository.login(email, password);

    }


    public LiveData<UserResponse> fazerCadastro(String name, String email, String password) {

        return repository.register(name, email, password);

    }

}