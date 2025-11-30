package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.ProductResponseDTO;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<Product> entities = service.findAll();

        List<ProductResponseDTO> dtos = entities.stream()
                .map(ProductResponseDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        Product entity = service.findById(id);
        ProductResponseDTO dto = new ProductResponseDTO(entity);
        return ResponseEntity.ok(dto);
    }
}
