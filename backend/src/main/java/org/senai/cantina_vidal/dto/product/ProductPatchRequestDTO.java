package org.senai.cantina_vidal.dto.product;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProductPatchRequestDTO(
        String name,

        String description,

        @PositiveOrZero(message = "Preço não pode ser negativo")
        BigDecimal price,

        String imageUrl,

        @PositiveOrZero(message = "Estoque não pode ser negativo")
        Integer quantityStock,

        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        Integer minStockLevel,

        @PositiveOrZero(message = "Intervalo de reposição não pode ser negativo")
        Integer replenishmentDays,

        @FutureOrPresent(message = "O produto deve estar na validade")
        LocalDate expirationDate,

        Set<Long> categoryIds
        ) {}
