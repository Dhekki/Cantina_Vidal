package org.senai.cantina_vidal.repository;

import lombok.NonNull;
import org.senai.cantina_vidal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
