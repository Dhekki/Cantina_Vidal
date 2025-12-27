package org.senai.cantina_vidal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.senai.cantina_vidal.dto.UserResponseDTO;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductResponseDTO;
import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserPatchRequestDTO dto);

    UserResponseDTO toDTO(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UserPatchRequestDTO dto, @MappingTarget User entity);
}
