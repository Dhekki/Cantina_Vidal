package com.example.projeto_v1.viewmodel;


import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;


import com.example.projeto_v1.model.UserResponse;

import com.example.projeto_v1.repository.UserRepository;


public class UserViewModel extends ViewModel {


    private final UserRepository repository;


    public UserViewModel() {

        this.repository = new UserRepository();

    }


    public LiveData<UserResponse> carregarPerfil(String token) {

// Se o token não tiver o prefixo Bearer, adicionamos aqui por segurança

        String formattedToken = token.startsWith("Bearer ") ? token : "Bearer " + token;

        return repository.getMyProfile(formattedToken);

    }

}