package com.example.projeto_v1.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.example.projeto_v1.model.Cliente;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepository {
    private ApiService apiService = (ApiService)RetrofitClient.getRetrofitInstance().create(ApiService.class);
    private final String API_KEY = "";
    private final String AUTH = "";

    // Adicionar quando o banco de dados estiver pronto.
    // private final String AUTH = "Bearer ";

    public MutableLiveData<Boolean> cadastrarCliente(Cliente cliente) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        // Conferir no LogCat se o cliente realmente foi cadastrado.
        Log.d("CADASTRO_MOCK", "Cliente simulado: " + cliente.getNome() +
                " | Email: " + cliente.getEmail() + " | Senha: " + cliente.getSenha());

        // Remover essa parte do código quando tiver a API_KEY.
        sucesso.setValue(true);
        return sucesso;

        // Descomentar essa parte do código quando tiver a API_KEY.
//        apiService.cadastrarCliente(API_KEY, AUTH, cliente).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                sucesso.setValue(response.isSuccessful());
//            }
//
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                sucesso.setValue(false);
//            }
//        });
//
//
//        return sucesso;
    }

    public MutableLiveData<Boolean> realizarLogin(Cliente cliente) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData<>();

        Log.d("LOGIN_MOCK", "Dados informados: " +
                " Email: " + cliente.getEmail() + " | Senha: " + cliente.getSenha());

        sucesso.setValue(true);
        return sucesso;


    }
}
