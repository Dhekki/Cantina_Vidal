package org.senai.cantina_vidal.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        String imageUrl
) {
}
