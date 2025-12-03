package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.projeto_v1.model.Cliente;
import com.example.projeto_v1.repository.ClienteRepository;
import java.util.List;

public class ClienteViewModel extends ViewModel {
    private final ClienteRepository repository = new ClienteRepository();
    private final MutableLiveData<List<Cliente>> clientes = new MutableLiveData();

    public LiveData<List<Cliente>> getClientes() {
        return this.clientes;
    }

    public void carregarClientes() {
        this.repository.listarClientes(this.clientes);
    }

//    public LiveData<Boolean> salvarProduto(String nome, Double preco) {
//        Produto p = new Produto(nome, preco);
//        return repository.inserirProduto(p);
}
