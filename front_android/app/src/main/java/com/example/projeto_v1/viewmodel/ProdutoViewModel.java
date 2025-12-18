package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projeto_v1.model.ProductCustomerResponse;
import com.example.projeto_v1.repository.ProdutoRepository;

import java.util.List;

public class ProdutoViewModel extends ViewModel {

    private final ProdutoRepository repository;

    public ProdutoViewModel() {
        repository = new ProdutoRepository();
    }

    public LiveData<List<ProductCustomerResponse>> listarProdutos(String token, String filtroNome, Long filtroCategoriaId) {
        return repository.getProducts(token, filtroNome, filtroCategoriaId);
    }

    public LiveData<ProductCustomerResponse> buscarProdutoPorId(String token, Long id) {
        return repository.getProductById(token, id);
    }
}