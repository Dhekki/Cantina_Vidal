package org.senai.cantina_vidal.repository;

import org.senai.cantina_vidal.entity.Order;
import org.senai.cantina_vidal.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT MAX(o.dailyId) FROM Order o WHERE o.createdAt BETWEEN :start AND :end")
    Optional<Integer> findMaxDailyIdOfDay(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            INSERT INTO daily_order_sequence (order_date, current_value) 
            VALUES (CURRENT_DATE, 1) 
            ON CONFLICT (order_date) 
            DO UPDATE SET current_value = daily_order_sequence.current_value + 1 
            RETURNING current_value
            """, nativeQuery = true)
    Integer generateAtomicDailyId();
}
