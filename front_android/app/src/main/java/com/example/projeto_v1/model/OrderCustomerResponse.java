package com.example.projeto_v1.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

// Usado especificamente no retorno da criação do pedido (tem pickupCode)
public class OrderCustomerResponse implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("dailyId")
    private Integer dailyId;

    @SerializedName("pickupCode")
    private String pickupCode; // Diferença principal

    @SerializedName("status")
    private String status;

    @SerializedName("total")
    private Double total;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("items")
    private List<OrderItemResponse> items;

    public OrderCustomerResponse() {}

    public Long getId() { return id; }
    public String getPickupCode() { return pickupCode; }
    // ... outros getters se necessário

    public Integer getDailyId() {
        return dailyId;
    }

    public void setDailyId(Integer dailyId) {
        this.dailyId = dailyId;
    }
}