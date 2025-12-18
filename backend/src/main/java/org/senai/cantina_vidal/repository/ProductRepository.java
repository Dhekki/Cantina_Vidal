package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @EntityGraph(attributePaths = {"categories"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query("""
                SELECT DISTINCT p FROM Product p 
                LEFT JOIN FETCH p.categories 
                WHERE p.available = true 
                AND p.quantityStock > 0 
                AND (:categoryId IS NULL OR :categoryId MEMBER OF p.categories)
                AND (:name IS NULL OR LOWER(p.name) LIKE :name) 
            """)
    Page<Product> findAllAvailableForCostumer(Pageable pageable, String name, Long categoryId);
}
