package org.senai.cantina_vidal.dto;

import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Set<String> categoriesNames
) {
    public ProductResponseDTO(Product entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCurrentPrice(),
                entity.getImageUrl(),
                (entity.getCategories() != null)
                ? entity.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet())
                : new HashSet<>()
        );
    }
}
