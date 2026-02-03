package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedFalse(Long id);
}
