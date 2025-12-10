package org.senai.cantina_vidal.dto.auth;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        String name,
        String role
) {
}
