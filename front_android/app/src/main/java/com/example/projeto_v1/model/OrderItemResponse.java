package com.example.projeto_v1.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class OrderItemResponse implements Serializable {

    @SerializedName("productId")
    private Long productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("unitPrice")
    private Double unitPrice;

    @SerializedName("subTotal")
    private Double subTotal;

    public OrderItemResponse() {}

    // Getters
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public Double getSubTotal() { return subTotal; }
}