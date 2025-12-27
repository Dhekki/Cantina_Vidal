package org.senai.cantina_vidal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.senai.cantina_vidal.dto.category.CategoryRequestDTO;
import org.senai.cantina_vidal.dto.category.CategoryResponseDTO;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductResponseDTO;
import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.Product;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDTO dto);

    CategoryResponseDTO toDTO(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromDTO(CategoryRequestDTO dto, @MappingTarget Category entity);
}
