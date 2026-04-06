package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.CategoryApi;
import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.service.CategoryService;
import org.senai.cantina_vidal.dto.category.CategoryRequestDTO;
import org.senai.cantina_vidal.dto.category.CategoryResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {
    private final CategoryService service;

    @Override
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<Category> categories = service.findAll();
        List<CategoryResponseDTO> dtos = categories.stream()
                .map(CategoryResponseDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new CategoryResponseDTO(service.findById(id)));
    }

    @Override
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto) {
        Category savedCategory = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new CategoryResponseDTO(savedCategory));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO dto) {
        return ResponseEntity.ok(new CategoryResponseDTO(service.update(id, dto)));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
