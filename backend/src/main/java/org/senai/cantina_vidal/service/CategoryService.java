package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.category.CategoryRequestDTO;
import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;
import org.senai.cantina_vidal.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository repository;

    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o id: " + id));
    }

    @Transactional
    public Category create(CategoryRequestDTO dto) {
        Category entity = Category.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .colorHex(dto.colorHex())
                .build();

        return repository.save(entity);
    }
}
