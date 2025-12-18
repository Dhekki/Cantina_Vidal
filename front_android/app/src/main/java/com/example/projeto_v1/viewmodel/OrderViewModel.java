package com.example.projeto_v1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projeto_v1.model.OrderCustomerResponse;
import com.example.projeto_v1.model.OrderResponse;
import com.example.projeto_v1.model.OrderItemRequest;
import com.example.projeto_v1.repository.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel {

    private final OrderRepository repository;

    public OrderViewModel() {
        this.repository = new OrderRepository();
    }

    public LiveData<List<OrderResponse>> listarMeusPedidos(String token) {
        return repository.getMyOrders(token);
    }

    // Método para finalizar o carrinho
    // Recebe a lista de itens (id e quantidade) e manda bala
    public LiveData<OrderCustomerResponse> realizarPedido(String token, List<OrderItemRequest> items) {
        return repository.createOrder(token, items);
    }
}