package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.entity.User;
import org.springframework.stereotype.Service;
import org.senai.cantina_vidal.mapper.UserMapper;
import org.senai.cantina_vidal.repository.UserRepository;
import org.senai.cantina_vidal.dto.user.UserPatchRequestDTO;
import org.springframework.transaction.annotation.Transactional;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public User findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
    }

    @Transactional
    public User update(Long id, UserPatchRequestDTO dto) {
        User entity = this.findById(id);

        mapper.updateUserFromDTO(dto, entity);

        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Usuário não encontrado com o id:" + id);

        repository.deleteById(id);
    }
}
