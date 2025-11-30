package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Product;
import org.senai.cantina_vidal.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
