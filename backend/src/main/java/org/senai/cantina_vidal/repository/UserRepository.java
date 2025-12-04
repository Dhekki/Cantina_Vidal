package org.senai.cantina_vidal.repository;

import lombok.NonNull;
import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> Role(Role role);

    List<User> findAllByRole(Role role);
}
