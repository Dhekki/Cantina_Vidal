package org.senai.cantina_vidal.dto;

import org.senai.cantina_vidal.entity.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String role,
        String imageUrl,
        Boolean active
) {
    public UserResponseDTO(User entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole().name(),
                entity.getImageUrl(),
                entity.getActive());
    }
}
