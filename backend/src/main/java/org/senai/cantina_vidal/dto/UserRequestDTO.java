package org.senai.cantina_vidal.dto;

import jakarta.validation.constraints.NotBlank;
import org.senai.cantina_vidal.enums.Role;

public record UserRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatório")
        String password,

        @NotBlank(message = "Cargo é obrigatório")
        Role role,

        String imageUrl
) {
}
