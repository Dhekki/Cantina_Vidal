package com.example.projeto_v1.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.projeto_v1.model.PageResponse;
import com.example.projeto_v1.model.ProductCustomerResponse;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoRepository { // Nome mantido como o original do projeto do seu amigo

    private final ApiService apiService;

    public ProdutoRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public MutableLiveData<List<ProductCustomerResponse>> getProducts(String token, String name, Long categoryId) {
        MutableLiveData<List<ProductCustomerResponse>> productsData = new MutableLiveData<>();

        String auth = (token != null && !token.startsWith("Bearer ")) ? "Bearer " + token : token;

        apiService.getProducts(auth, name, categoryId, 0, 100, "name,asc").enqueue(new Callback<PageResponse<ProductCustomerResponse>>() {
            @Override
            public void onResponse(Call<PageResponse<ProductCustomerResponse>> call, Response<PageResponse<ProductCustomerResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsData.setValue(response.body().getContent());
                } else {
                    productsData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PageResponse<ProductCustomerResponse>> call, Throwable t) {
                productsData.setValue(null);
            }
        });

        return productsData;
    }

    public MutableLiveData<ProductCustomerResponse> getProductById(String token, Long id) {
        MutableLiveData<ProductCustomerResponse> productData = new MutableLiveData<>();
        String auth = (token != null && !token.startsWith("Bearer ")) ? "Bearer " + token : token;

        apiService.getProductById(auth, id).enqueue(new Callback<ProductCustomerResponse>() {
            @Override
            public void onResponse(Call<ProductCustomerResponse> call, Response<ProductCustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productData.setValue(response.body());
                } else {
                    productData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProductCustomerResponse> call, Throwable t) {
                productData.setValue(null);
            }
        });
        return productData;
    }
}