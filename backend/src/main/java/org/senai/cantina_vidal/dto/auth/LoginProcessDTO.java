package org.senai.cantina_vidal.dto.auth;

public record LoginProcessDTO(
        String accessToken,
        String refreshToken,
        String name,
        String role
) {}
