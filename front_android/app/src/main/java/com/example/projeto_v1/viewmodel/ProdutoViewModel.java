package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;
import com.example.projeto_v1.repository.ProdutoRepository;

import java.util.List;

public class ProdutoViewModel extends ViewModel {
    private ApiService apiService;
    private final ProdutoRepository repository;
    private final MutableLiveData<List<Produto>> produtos = new MutableLiveData<>();

    private final String API_KEY = "";
    private final String AUTH = "";
    public ProdutoViewModel() {
        repository = new ProdutoRepository();

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }
}
