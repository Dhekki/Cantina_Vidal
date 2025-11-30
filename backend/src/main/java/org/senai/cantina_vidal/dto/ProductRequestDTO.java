package org.senai.cantina_vidal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Nome obrigatório")
        String name,

        String description,

        @NotNull @Positive
        BigDecimal price,

        String imageUrl,

        @NotNull(message = "ID da categoria é obrigatório")
        Long categoryId
        ) {}
