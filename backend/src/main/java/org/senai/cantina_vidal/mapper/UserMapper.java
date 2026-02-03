package org.senai.cantina_vidal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.dto.user.UserResponseDTO;
import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserPatchRequestDTO dto);

    UserResponseDTO toDTO(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UserPatchRequestDTO dto, @MappingTarget User entity);
}
