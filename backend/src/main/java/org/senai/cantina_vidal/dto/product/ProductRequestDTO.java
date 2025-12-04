package org.senai.cantina_vidal.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Set;

public record ProductRequestDTO(
        @NotBlank(message = "Nome obrigatório")
        String name,

        String description,

        @NotNull(message = "Preço é obrigatório")
        @PositiveOrZero(message = "Preço não pode ser negativo")
        BigDecimal price,

        String imageUrl,

        @NotNull(message = "Estoque é obrigatório")
        @PositiveOrZero(message = "Estoque não pode ser negativo")
        Integer quantityStock,

        Set<Long> categoryIds
        ) {}
