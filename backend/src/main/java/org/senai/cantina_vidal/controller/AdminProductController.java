package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductResponseDTO;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable
    ) {
        Page<Product> productPage = service.findAllAdmin(pageable, name, categoryId);

        Page<ProductResponseDTO> dtoPage = productPage.map(ProductResponseDTO::new);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ProductResponseDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        Product savedProduct = service.create(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new ProductResponseDTO(savedProduct));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductPatchRequestDTO dto) {
        return ResponseEntity.ok(new ProductResponseDTO(service.update(id, dto)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(
            @PathVariable Long id) {
        service.toggleAvailability(id);
        return ResponseEntity.noContent().build();
    }
}
