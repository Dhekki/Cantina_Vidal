package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.projeto_v1.model.Categoria;
import com.example.projeto_v1.model.Produto;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;
import com.example.projeto_v1.repository.CategoriaRepository;
import com.example.projeto_v1.repository.ProdutoRepository;

import java.util.List;

public class CategoriaViewModel {
    private ApiService apiService;
    private final CategoriaRepository repository;
    private final MutableLiveData<List<Categoria>> categorias = new MutableLiveData<>();

    private final String API_KEY = "";
    private final String AUTH = "";

    public CategoriaViewModel() {
        repository = new CategoriaRepository();

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }
}
