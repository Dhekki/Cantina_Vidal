package com.example.projeto_v1.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.projeto_v1.model.CategoryResponse;
import com.example.projeto_v1.model.PageResponse;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriaRepository {

    private final ApiService apiService;

    public CategoriaRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public MutableLiveData<List<CategoryResponse>> getCategories() {
        MutableLiveData<List<CategoryResponse>> data = new MutableLiveData<>();

        // Buscando página 0, tamanho 50 (pra pegar todas de vez), ordenado por nome
        apiService.getCategories(0, 50, "name,asc").enqueue(new Callback<PageResponse<CategoryResponse>>() {
            @Override
            public void onResponse(Call<PageResponse<CategoryResponse>> call, Response<PageResponse<CategoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Extrai a lista de dentro do PageResponse
                    data.setValue(response.body().getContent());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PageResponse<CategoryResponse>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}