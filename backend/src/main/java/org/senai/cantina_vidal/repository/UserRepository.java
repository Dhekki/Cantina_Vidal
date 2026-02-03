package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);
}
