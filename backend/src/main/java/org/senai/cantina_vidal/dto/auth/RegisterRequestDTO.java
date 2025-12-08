package org.senai.cantina_vidal.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Pattern(
                regexp = "^(?i)(?!d[aeo]s?\\b)\\p{L}+(?:\\s+\\p{L}+)+\\s+(?!d[aeo]s?\\b)\\p{L}+$",
                message = "Digite seu nome completo (Nome + Sobrenome)"
        )
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@#\\-_]).{8,}$",
                message = "Senha fraca. Mínimo: 8 caracteres contendo: " +
                        "1 letra Maiúscula, 1 letra Minúscula, 1 Número e 1 Símbolo ($@#-_)"
        )
        String password
) {
}
