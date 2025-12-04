package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;
import org.senai.cantina_vidal.repository.CategoryRepository;
import org.senai.cantina_vidal.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));
    }

    @Transactional
    public Product create(ProductRequestDTO dto) {
        Product entity = Product.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .currentPrice(dto.price())
                .description(dto.description())
                .quantityStock(dto.quantityStock())
                .build();

        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.categoryIds());

            if (categories.size() != dto.categoryIds().size())
                throw new ResourceNotFoundException("Uma ou mais categorias não existem");

            entity.setCategories(new HashSet<>(categories));
        }


        return repository.save(entity);
    }
}
