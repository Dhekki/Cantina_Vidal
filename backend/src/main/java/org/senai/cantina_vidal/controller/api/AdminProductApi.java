package org.senai.cantina_vidal.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "2. Produtos (Admin)", description = "Gestão completa de produtos (CRUD)")
@SecurityRequirement(name = "cookieAuth")
public interface AdminProductApi {

    @Operation(summary = "Listar todos (Admin)", description = "Lista todos os produtos cadastrados.")
    ResponseEntity<List<ProductResponseDTO>> findAll();

    @Operation(summary = "Buscar por ID (Admin)", description = "Busca detalhes completos de um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<ProductResponseDTO> findById(Long id);

    @Operation(summary = "Criar Produto", description = "Cadastra um novo item no cardápio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria vinculada não encontrada")
    })
    ResponseEntity<ProductResponseDTO> create(ProductRequestDTO dto);

    @Operation(summary = "Atualizar Produto", description = "Atualiza dados de um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<ProductResponseDTO> update(Long id, ProductPatchRequestDTO dto);

    @Operation(summary = "Deletar Produto", description = "Realiza a exclusão lógica (Soft Delete).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto deletado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<Void> delete(Long id);

    @Operation(summary = "Alternar Disponibilidade", description = "Ativa ou desativa um produto rapidamente (Toggle).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disponibilidade alterada"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<Void> toggle(Long id);
}
