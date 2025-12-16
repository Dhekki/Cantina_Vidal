package org.senai.cantina_vidal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.product.ProductCustomerResponseDTO;
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
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductCustomerResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable
    ) {
        Page<Product> productPage = service.findAllCustomer(pageable, name, categoryId);

        Page<ProductCustomerResponseDTO> dtoPage = productPage.map(ProductCustomerResponseDTO::new);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCustomerResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ProductCustomerResponseDTO(service.findByIdCustomer(id)));
    }
}
