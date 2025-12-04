package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projeto_v1.model.Cliente;
import com.example.projeto_v1.repository.ClienteRepository;

import java.util.List;

public class ClienteViewModel extends ViewModel {
    private final ClienteRepository repository;
    private final MutableLiveData<List<Cliente>> clientes = new MutableLiveData<>();

    public ClienteViewModel() {
        repository = new ClienteRepository();
    }

    public LiveData<List<Cliente>> getClientes() {
        return clientes;
    }

    public void carregarClientes() {
        this.repository.listarClientes(this.clientes);
    }

    public LiveData<Boolean> salvarCliente(String nome, String email, String senha) {
        Cliente c = new Cliente(nome, email, senha);
        return repository.inserirCliente(c);
    }
}
