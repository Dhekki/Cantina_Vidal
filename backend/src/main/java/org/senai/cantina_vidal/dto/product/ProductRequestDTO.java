package org.senai.cantina_vidal.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProductRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        String description,

        @NotNull(message = "Preço é obrigatório")
        @PositiveOrZero(message = "Preço não pode ser negativo")
        BigDecimal price,

        String imageUrl,

        @NotNull(message = "Estoque é obrigatório")
        @PositiveOrZero(message = "Estoque não pode ser negativo")
        Integer quantityStock,

        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        Integer minStockLevel,

        @PositiveOrZero(message = "Intervalo de reposição não pode ser negativo")
        Integer replenishmentDays,

        LocalDate expirationDate,

        Set<Long> categoryIds
        ) {}
