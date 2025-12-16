package org.senai.cantina_vidal.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserPatchRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Pattern(
                regexp = "^(?i)(?!d[aeo]s?\\b)\\p{L}+(?:\\s+\\p{L}+)+\\s+(?!d[aeo]s?\\b)\\p{L}+$",
                message = "Digite seu nome completo (Nome + Sobrenome)"
        )
        String name,

        String imageUrl
) {
}
