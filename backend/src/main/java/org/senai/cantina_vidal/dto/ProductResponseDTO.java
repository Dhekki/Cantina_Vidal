package org.senai.cantina_vidal.dto;

import org.senai.cantina_vidal.entity.Product;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id,
                                 String name,
                                 String description,
                                 BigDecimal price,
                                 String imageUrl,
                                 String categoryName) {
    public ProductResponseDTO(Product entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCurrentPrice(),
                entity.getImageUrl(),
                entity.getCategory().getName()
        );

    }
}
