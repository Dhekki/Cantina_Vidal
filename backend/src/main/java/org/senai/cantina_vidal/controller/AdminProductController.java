package org.senai.cantina_vidal.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.service.ProductService;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductResponseDTO;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
@Tag(name = "2. Produtos (Admin)", description = "Gestão completa de produtos (CRUD)")
@SecurityRequirement(name = "cookieAuth")
public class AdminProductController {
    private final ProductService service;

    @Operation(summary = "Listar todos (Admin)", description = "Lista todos os produtos cadastrados.")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<Product> products = service.findAllForAdmin();

        List<ProductResponseDTO> dtos = products.stream()
                .map(ProductResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar por ID (Admin)", description = "Busca detalhes completos de um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ProductResponseDTO(service.findByIdForAdmin(id)));
    }

    @Operation(summary = "Criar Produto", description = "Cadastra um novo item no cardápio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria vinculada não encontrada")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        Product savedProduct = service.create(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new ProductResponseDTO(savedProduct));
    }

    @Operation(summary = "Atualizar Produto", description = "Atualiza dados de um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductPatchRequestDTO dto) {
        return ResponseEntity.ok(new ProductResponseDTO(service.update(id, dto)));
    }

    @Operation(summary = "Deletar Produto", description = "Realiza a exclusão lógica (Soft Delete).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto deletado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Alternar Disponibilidade", description = "Ativa ou desativa um produto rapidamente (Toogle).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disponibilidade alterada"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(
            @PathVariable Long id) {
        service.toggleAvailability(id);
        return ResponseEntity.noContent().build();
    }
}
