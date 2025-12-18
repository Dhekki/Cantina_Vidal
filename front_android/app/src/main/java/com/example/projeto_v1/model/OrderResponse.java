package com.example.projeto_v1.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

// Usado na listagem e detalhes gerais
public class OrderResponse implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("dailyId")
    private Integer dailyId;

    @SerializedName("status")
    private String status;

    @SerializedName("total")
    private Double total;

    @SerializedName("createdAt")
    private String createdAt; // Vem como String ISO do backend

    @SerializedName("items")
    private List<OrderItemResponse> items;

    public OrderResponse() {}

    // Getters e Setters
    public Long getId() { return id; }
    public Integer getDailyId() { return dailyId; }
    public String getStatus() { return status; }
    public Double getTotal() { return total; }
    public String getCreatedAt() { return createdAt; }
    public List<OrderItemResponse> getItems() { return items; }
}