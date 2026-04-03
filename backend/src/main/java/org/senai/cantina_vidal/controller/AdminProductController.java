package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.AdminProductApi;
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
public class AdminProductController implements AdminProductApi {
    private final ProductService service;

    @Override
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<Product> products = service.findAllForAdmin();

        List<ProductResponseDTO> dtos = products.stream()
                .map(ProductResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ProductResponseDTO(service.findByIdForAdmin(id)));
    }

    @Override
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        Product savedProduct = service.create(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new ProductResponseDTO(savedProduct));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductPatchRequestDTO dto) {
        return ResponseEntity.ok(new ProductResponseDTO(service.update(id, dto)));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Long id) {
        service.toggleAvailability(id);
        return ResponseEntity.noContent().build();
    }
}
