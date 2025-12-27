package org.senai.cantina_vidal.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequestDTO(
        @NotNull(message = "ID do produto é obrigatório")
        Long productId,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser pelo menos 1")
        Integer quantity
) {
}
