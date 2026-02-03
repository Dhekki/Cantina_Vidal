package org.senai.cantina_vidal.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AccessTokenRequestDTO(
        @NotBlank
        String accessToken
) {
}
