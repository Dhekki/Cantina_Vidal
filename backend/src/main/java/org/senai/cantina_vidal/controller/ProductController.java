package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.service.ProductService;
import org.senai.cantina_vidal.dto.product.ProductCustomerResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "2. Produtos (Catálogo)", description = "Visualização pública de produtos disponíveis")
public class ProductController {
    private final ProductService service;

    @Operation(summary = "Cardápio do Dia", description = "Lista produtos disponíveis, com estoque > 0 e não deletados.")
    @GetMapping
    public ResponseEntity<List<ProductCustomerResponseDTO>> findAll() {
        List<Product> products = service.findAllForCustomer();

        List<ProductCustomerResponseDTO> dtos = products.stream()
                .map(ProductCustomerResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Detalhes do Produto", description = "Exibe detalhes de um item específico do cardápio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado ou indisponível")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductCustomerResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ProductCustomerResponseDTO(service.findByIdForCustomer(id)));
    }
}
