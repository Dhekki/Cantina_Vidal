package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Category;
import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
