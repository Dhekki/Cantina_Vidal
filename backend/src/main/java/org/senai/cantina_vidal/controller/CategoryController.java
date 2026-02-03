package org.senai.cantina_vidal.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.service.CategoryService;
import org.senai.cantina_vidal.dto.category.CategoryRequestDTO;
import org.senai.cantina_vidal.dto.category.CategoryResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "7. Categorias", description = "Catálogo de categorias de produtos")
public class CategoryController {
    private final CategoryService service;

    @Operation(summary = "Listar Categorias", description = "Endpoint público. Lista apenas categorias ativas.")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {

        List<Category> categories = service.findAll();

        List<CategoryResponseDTO> dtos = categories.stream()
                .map(CategoryResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar por ID", description = "Endpoint público.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new CategoryResponseDTO(service.findById(id)));
    }

    @Operation(summary = "Criar Categoria", description = "Requer permissão de ADMIN ou EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @SecurityRequirement(name = "bearer-key")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto) {
        Category savedCategory = service.create(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new CategoryResponseDTO(savedCategory));
    }

    @Operation(summary = "Atualizar Categoria", description = "Requer permissão de ADMIN ou EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @SecurityRequirement(name = "bearer-key")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO dto) {
        return ResponseEntity.ok(new CategoryResponseDTO(service.update(id, dto)));
    }

    @Operation(summary = "Deletar Categoria", description = "Soft Delete. Requer permissão de ADMIN ou EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
