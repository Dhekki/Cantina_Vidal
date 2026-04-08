package org.senai.cantina_vidal.controller;

import lombok.RequiredArgsConstructor;

import org.senai.cantina_vidal.controller.api.ProductApi;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.service.ProductService;
import org.senai.cantina_vidal.dto.product.ProductCustomerResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController implements ProductApi {
    private final ProductService service;

    @Override
    @GetMapping
    public ResponseEntity<List<ProductCustomerResponseDTO>> findAll() {
        List<Product> products = service.findAllForCustomer();

        List<ProductCustomerResponseDTO> dtos = products.stream()
                .map(ProductCustomerResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductCustomerResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ProductCustomerResponseDTO(service.findByIdForCustomer(id)));
    }
}
