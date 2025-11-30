package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
