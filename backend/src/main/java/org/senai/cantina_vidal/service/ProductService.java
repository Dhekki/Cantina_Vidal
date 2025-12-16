package org.senai.cantina_vidal.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;
import org.senai.cantina_vidal.mapper.ProductMapper;
import org.senai.cantina_vidal.repository.CategoryRepository;
import org.senai.cantina_vidal.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

//    public Page<Product> findAll(Pageable pageable, String name, Long categoryId) {
//        if (name != null && !name.isBlank())
//            return repository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable);
//
//        if (categoryId != null)
//            return repository.findByCategoriesIdAndActiveTrue(categoryId, pageable);
//
//        return repository.findAll(pageable);
//    }

    public Page<Product> findAllAdmin(Pageable pageable, String name, Long categoryId) {
        // Specification é uma forma de criar WHERE dinâmico
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro de Nome (se vier)
            if (name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%")
                );
            }

            // 2. Filtro de Categoria (se vier)
            if (categoryId != null) {
                // Join com categorias para filtrar
                predicates.add(criteriaBuilder.equal(
                        root.join("categories").get("id"),
                        categoryId)
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Precisa que o Repository estenda JpaSpecificationExecutor<Product>
        return repository.findAll(spec, pageable);
    }

    public Page<Product> findAllCustomer(Pageable pageable, String name, Long categoryId) {
        return repository.findAllAvailableForCostumer(pageable, name, categoryId);
    }



    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));
    }

    public Product findByIdCustomer(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        if (!entity.getAvailable() || entity.getQuantityStock() == 0)
            throw new ResourceNotFoundException("Produto não encontrado com o id: " + id);

        return entity;
    }

    @Transactional
    public Product create(ProductRequestDTO dto) {
        Product entity = Product.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .currentPrice(dto.price())
                .description(dto.description())
                .quantityStock(dto.quantityStock())
                .minStockLevel(dto.minStockLevel())
                .replenishmentDays(dto.replenishmentDays())
                .expirationDate(dto.expirationDate())
                .build();

        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.categoryIds());

            if (categories.size() != dto.categoryIds().size())
                throw new ResourceNotFoundException("Uma ou mais categorias não existem");

            entity.setCategories(new HashSet<>(categories));
        }

        return repository.save(entity);
    }

    @Transactional
    public Product update(Long id, ProductPatchRequestDTO dto) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        mapper.updateProductFromDTO(dto, entity);

        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Produto não encontrado com o id: " + id);

        repository.deleteById(id);
    }

    @Transactional
    public void toggleAvailability(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        entity.setAvailable(!entity.getAvailable());

        repository.save(entity);
    }
}
