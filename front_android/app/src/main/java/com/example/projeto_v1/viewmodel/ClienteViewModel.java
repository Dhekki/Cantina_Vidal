package com.example.projeto_v1.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projeto_v1.model.Cliente;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;
import com.example.projeto_v1.repository.ClienteRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteViewModel extends ViewModel {
    private ApiService apiService;
    private final ClienteRepository repository;
    private final MutableLiveData<List<Cliente>> clientes = new MutableLiveData<>();

    private final String API_KEY = "";
    private final String AUTH = "";
    public ClienteViewModel() {
        repository = new ClienteRepository();

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<List<Cliente>> getClientes() {
        return clientes;
    }

//    public void carregarClientes() {
//        this.repository.listarClientes(this.clientes);
//    }

    public LiveData<Boolean> cadastrarCliente(String nome, String email, String senha) {
        Cliente c = new Cliente(nome, email, senha);
        return repository.cadastrarCliente(c);
    }

    public LiveData<Boolean> realizarLogin(String email, String senha) {
            MutableLiveData<Boolean> resultado = new MutableLiveData<>();

            apiService.realizarLogin(API_KEY, AUTH, email, senha).enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cliente> clientesEncontrados = response.body();
                    if (!clientesEncontrados.isEmpty()) {
                        Cliente c = clientesEncontrados.get(0);

                        resultado.setValue(true);
                    } else {
                        resultado.setValue(false);
                    }
                } else {
                    resultado.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                resultado.setValue(false);
            }
        });

        return resultado;
    }
}
