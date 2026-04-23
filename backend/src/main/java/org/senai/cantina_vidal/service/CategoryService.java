package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.event.CatalogRefreshEvent;
import org.senai.cantina_vidal.repository.CategoryRepository;
import org.senai.cantina_vidal.dto.category.CategoryRequestDTO;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public List<Category> findAll() {
        return repository.findByDeletedFalse();
    }

    public Category findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o id: " + id));
    }

    @Transactional
    public Category create(CategoryRequestDTO dto) {
        Category entity = Category.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .colorHex(dto.colorHex())
                .build();

        eventPublisher.publishEvent(new CatalogRefreshEvent());
        return repository.save(entity);
    }

    @Transactional
    public Category update(Long id, CategoryRequestDTO dto) {
        Category entity = findById(id);

        entity.update(dto.name(), dto.imageUrl(), dto.colorHex());
        eventPublisher.publishEvent(new CatalogRefreshEvent());

        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Categoria não encontrada com o id: " + id);

        repository.deleteById(id);
        eventPublisher.publishEvent(new CatalogRefreshEvent());
    }
}
