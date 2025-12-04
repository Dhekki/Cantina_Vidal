package org.senai.cantina_vidal.dto.category;

import org.senai.cantina_vidal.entity.Category;

public record CategoryResponseDTO(
        Long id,
        String name,
        String imageUrl,
        Boolean active
) {
    public CategoryResponseDTO(Category entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getImageUrl(),
                entity.getActive());
    }
}
