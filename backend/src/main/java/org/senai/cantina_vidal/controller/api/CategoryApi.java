package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.category.CategoryRequestDTO;
import org.senai.cantina_vidal.dto.category.CategoryResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "7. Categorias", description = "Catálogo de categorias de produtos")
public interface CategoryApi {

    @Operation(summary = "Listar Categorias", description = "Endpoint público. Lista apenas categorias ativas.")
    ResponseEntity<List<CategoryResponseDTO>> findAll();

    @Operation(summary = "Buscar por ID", description = "Endpoint público.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    ResponseEntity<CategoryResponseDTO> findById(Long id);

    @Operation(summary = "Criar Categoria", description = "Requer permissão de ADMIN ou EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @SecurityRequirement(name = "cookieAuth") // Atualizado de bearer-key para cookieAuth
    ResponseEntity<CategoryResponseDTO> create(CategoryRequestDTO dto);

    @Operation(summary = "Atualizar Categoria", description = "Requer permissão de ADMIN ou EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @SecurityRequirement(name = "cookieAuth") // Atualizado de bearer-key para cookieAuth
    ResponseEntity<CategoryResponseDTO> update(Long id, CategoryRequestDTO dto);

    @Operation(summary = "Deletar Categoria", description = "Soft Delete. Requer permissão de ADMIN ou EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @SecurityRequirement(name = "cookieAuth") // Atualizado de bearer-key para cookieAuth
    ResponseEntity<Void> delete(Long id);
}
