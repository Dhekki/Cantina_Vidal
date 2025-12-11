package org.senai.cantina_vidal.dto.product;

import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Integer quantityStock,
        Integer minStockLevel,
        Integer replenishmentDays,
        LocalDate expirationDate,
        Boolean active,
        Set<String> categoriesNames
) {
    public ProductResponseDTO(Product entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCurrentPrice(),
                entity.getImageUrl(),
                entity.getQuantityStock(),
                entity.getMinStockLevel(),
                entity.getReplenishmentDays(),
                entity.getExpirationDate(),
                entity.getActive(),
                entity.getCategories() != null
                        ? entity.getCategories().stream()
                            .map(Category::getName)
                            .collect(Collectors.toSet())
                        : Collections.emptySet()
        );
    }
}
