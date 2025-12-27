package org.senai.cantina_vidal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductResponseDTO;
import org.senai.cantina_vidal.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductPatchRequestDTO dto);

    ProductResponseDTO toDTO(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDTO(ProductPatchRequestDTO dto, @MappingTarget Product entity);
}
