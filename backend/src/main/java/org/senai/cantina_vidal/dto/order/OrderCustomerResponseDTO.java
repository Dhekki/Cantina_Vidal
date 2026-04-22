package org.senai.cantina_vidal.dto.order;

import org.senai.cantina_vidal.entity.Order;

import java.util.List;
import java.util.Collections;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCustomerResponseDTO(
        Long id,
        Integer dailyId,
        String pickupCode,
        String status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items
) {
    public OrderCustomerResponseDTO(Order entity) {
        this(
                entity.getId(),
                entity.getDailyId(),
                entity.getPickupCode(),
                entity.getStatus().name(),
                entity.getTotal(),
                entity.getCreatedAt(),
                entity.getItems() != null
                        ? entity.getItems().stream()
                            .map(OrderItemResponseDTO::new)
                            .toList()
                        : Collections.emptyList());
    }
}
