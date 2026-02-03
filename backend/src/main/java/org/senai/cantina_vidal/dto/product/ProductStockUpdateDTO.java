package org.senai.cantina_vidal.dto.product;

public record ProductStockUpdateDTO(
        Long id,
        Integer realStock,
        Boolean available
) {
}
