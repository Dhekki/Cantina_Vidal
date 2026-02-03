package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Category;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByDeletedFalse();

    Optional<Category> findByIdAndDeletedFalse(Long id);

    List<Category> findAllByIdInAndDeletedFalse(Collection<Long> ids);
}
