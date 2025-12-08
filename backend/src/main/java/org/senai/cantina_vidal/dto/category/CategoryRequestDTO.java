package org.senai.cantina_vidal.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        String imageUrl,

        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                message = "Formato de cor inválido")
        String colorHex
) {
}
