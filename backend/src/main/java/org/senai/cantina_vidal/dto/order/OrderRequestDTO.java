package org.senai.cantina_vidal.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDTO(
        @Valid
        @NotEmpty(message = "O pedido não pode estar vazio")
        List<OrderItemRequestDTO> items
) {
}
