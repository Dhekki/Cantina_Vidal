package com.example.projeto_v1.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class OrderItemRequest implements Serializable {

    @SerializedName("productId")
    private Long productId;

    @SerializedName("quantity")
    private Integer quantity;

    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters e Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}