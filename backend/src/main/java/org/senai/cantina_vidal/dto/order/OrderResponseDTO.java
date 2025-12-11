package org.senai.cantina_vidal.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.senai.cantina_vidal.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String status,
        BigDecimal total,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        LocalDateTime createdAt,

        List<OrderItemResponseDTO> items
) {
    public OrderResponseDTO(Order entity) {
        this(entity.getId(),
                entity.getStatus(),
                entity.getTotal(),
                entity.getCreatedAt(),
                entity.getItems() != null
                        ? entity.getItems().stream()
                            .map(OrderItemResponseDTO::new)
                            .toList()
                        : Collections.emptyList());
    }
}
