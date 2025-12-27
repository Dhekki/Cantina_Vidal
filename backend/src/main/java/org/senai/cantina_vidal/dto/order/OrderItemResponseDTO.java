package org.senai.cantina_vidal.dto.order;

import org.senai.cantina_vidal.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
    public OrderItemResponseDTO(OrderItem entity) {
        this(   entity.getId(),
                entity.getProduct().getName(),
                entity.getQuantity(),
                entity.getFrozenUnitPrice(),
                entity.getFrozenUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity()))
        );
    }
}
