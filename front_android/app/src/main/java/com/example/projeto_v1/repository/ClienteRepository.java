package com.example.projeto_v1.repository;

import androidx.lifecycle.MutableLiveData;
import com.example.projeto_v1.model.Cliente;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;
import java.util.List;

public class ClienteRepository {
    private ApiService apiService = (ApiService)RetrofitClient.getRetrofitInstance().create(ApiService.class);
    private final String API_KEY = "";
    private final String AUTH = "Bearer ";

    public void listarClientes(MutableLiveData<List<Cliente>> clientesLiveData) {
    }

    public MutableLiveData<Boolean> inserirCliente(Cliente cliente) {
        MutableLiveData<Boolean> sucesso = new MutableLiveData();
        return sucesso;
    }
}
