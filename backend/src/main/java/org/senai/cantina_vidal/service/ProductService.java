package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.QProduct;
import org.senai.cantina_vidal.entity.QCategory;
import org.senai.cantina_vidal.repository.ProductRepository;
import org.senai.cantina_vidal.repository.CategoryRepository;
import org.senai.cantina_vidal.dto.product.ProductRequestDTO;
import org.senai.cantina_vidal.dto.product.ProductPatchRequestDTO;
import org.senai.cantina_vidal.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository repository;
    private final JPAQueryFactory queryFactory;
    private final CategoryRepository categoryRepository;

    private static final QProduct qProduct = QProduct.product;
    private static final QCategory qCategory = QCategory.category;
    private final SseService sseService;

    public List<Product> findAllForAdmin() {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.categories, qCategory)
                .fetchJoin()
                .where(qProduct.deleted.isFalse())
                .distinct()
                .fetch();
    }

    public List<Product> findAllForCustomer() {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.categories, qCategory)
                .fetchJoin()
                .where(
                        qProduct.deleted.isFalse(),
                        qProduct.available.isTrue(),
                        qProduct.realStock.gt(0)
                )
                .distinct()
                .fetch();
    }


    public Product findByIdForAdmin(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.categories, qCategory)
                .fetchJoin()
                .where(
                        qProduct.id.eq(id),
                        qProduct.deleted.isFalse()
                )
                .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));
    }

    public Product findByIdForCustomer(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.categories, qCategory)
                .fetchJoin()
                .where(
                        qProduct.id.eq(id),
                        qProduct.deleted.isFalse(),
                        qProduct.available.isTrue(),
                        qProduct.realStock.gt(0)
                )
                .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));
    }

    @Transactional
    public Product create(ProductRequestDTO dto) {
        Set<Category> productCategories = new HashSet<>();

        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllByIdInAndDeletedFalse(dto.categoryIds());

            if (categories.size() != dto.categoryIds().size())
                throw new ResourceNotFoundException("Uma ou mais categorias não existem");

            productCategories = new HashSet<>(categories);
        }

        Product entity = Product.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .currentPrice(dto.price())
                .description(dto.description())
                .categories(productCategories)
                .quantityStock(dto.quantityStock())
                .minStockLevel(dto.minStockLevel())
                .expirationDate(dto.expirationDate())
                .replenishmentDays(dto.replenishmentDays())
                .build();

        Product savedEntity = repository.save(entity);
        sseService.notifyProductUpdate(savedEntity.getId(), savedEntity.getRealStock(), savedEntity.getAvailable());
        return savedEntity;
    }

    @Transactional
    public Product update(Long id, ProductPatchRequestDTO dto) {
        Product entity = findByIdForAdmin(id);

        entity.update(
                dto.name(), dto.description(), dto.price(), dto.imageUrl(),
                dto.quantityStock(), dto.minStockLevel(), dto.replenishmentDays(), dto.expirationDate()
        );

        if (dto.categoryIds() != null) {
            Set<Category> newCategories = new HashSet<>();

            if (!dto.categoryIds().isEmpty()) {
                List<Category> fetchedCategories = categoryRepository.findAllByIdInAndDeletedFalse(dto.categoryIds());
                if (fetchedCategories.size() != dto.categoryIds().size()) {
                    throw new ResourceNotFoundException("Uma ou mais categorias não existem");
                }
                newCategories.addAll(fetchedCategories);
            }

            entity.updateCategories(newCategories);
        }

        Product savedEntity = repository.save(entity);

        sseService.notifyProductUpdate(savedEntity.getId(), savedEntity.getRealStock(), savedEntity.getAvailable());

        return savedEntity;
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Produto não encontrado com o id: " + id);

        sseService.notifyProductUpdate(id, 0, false);
        repository.deleteById(id);
    }

    @Transactional
    public void toggleAvailability(Long id) {
        Product entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        entity.toggleAvailability();
        Product savedEntity = repository.save(entity);

        sseService.notifyProductUpdate(savedEntity.getId(), savedEntity.getRealStock(), savedEntity.getAvailable());
    }

    public List<Product> findAllById(Set<Long> ids) {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.categories, qCategory)
                .fetchJoin()
                .where(
                        qProduct.id.in(ids),
                        qProduct.deleted.isFalse())
                .distinct()
                .fetch();
    }
}
