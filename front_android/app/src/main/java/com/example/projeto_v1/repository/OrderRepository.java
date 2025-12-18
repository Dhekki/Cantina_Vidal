package com.example.projeto_v1.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.projeto_v1.model.OrderCustomerResponse;
import com.example.projeto_v1.model.OrderRequest;
import com.example.projeto_v1.model.OrderResponse;
import com.example.projeto_v1.model.OrderItemRequest;
import com.example.projeto_v1.network.ApiService;
import com.example.projeto_v1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    private final ApiService apiService;

    public OrderRepository() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public MutableLiveData<List<OrderResponse>> getMyOrders(String token) {
        MutableLiveData<List<OrderResponse>> data = new MutableLiveData<>();
        String auth = (token != null && !token.startsWith("Bearer ")) ? "Bearer " + token : token;

        apiService.getMyOrders(auth).enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<OrderCustomerResponse> createOrder(String token, List<OrderItemRequest> items) {
        MutableLiveData<OrderCustomerResponse> data = new MutableLiveData<>();
        String auth = (token != null && !token.startsWith("Bearer ")) ? "Bearer " + token : token;

        OrderRequest request = new OrderRequest(items);

        apiService.createOrder(auth, request).enqueue(new Callback<OrderCustomerResponse>() {
            @Override
            public void onResponse(Call<OrderCustomerResponse> call, Response<OrderCustomerResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<OrderCustomerResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}