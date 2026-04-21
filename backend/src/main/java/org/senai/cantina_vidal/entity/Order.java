package org.senai.cantina_vidal.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.senai.cantina_vidal.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "orders")
public class Order extends Auditable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    @Setter
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "pickup_code", length = 3)
    private String pickupCode;

    @Column(name = "daily_id")
    private Integer dailyId;

    @Column(name = "scheduled_pickup_time")
    private LocalDateTime scheduledPickupTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(Product product, Integer quantity) {
        OrderItem item = OrderItem.builder()
                .order(this)
                .product(product)
                .quantity(quantity)
                .frozenUnitPrice(product.getCurrentPrice())
                .build();

        items.add(item);
    }

    public void calculateTotal() {
        total = items.stream()
                .map(item -> item.getFrozenUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cancel() {
        if (status.isTerminal())
            throw new IllegalStateException("Pedidos finalizados não podem ser cancelados");

        status = OrderStatus.CANCELLED;
    }
}
