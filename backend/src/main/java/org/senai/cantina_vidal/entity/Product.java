package org.senai.cantina_vidal.entity;

import lombok.*;

import jakarta.persistence.*;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.senai.cantina_vidal.exception.ConflictException;

import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor @AllArgsConstructor
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
@Table(name = "products")
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Builder.Default
    @ColumnDefault("0.0")
    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice = BigDecimal.valueOf(0.0);

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Builder.Default
    @ColumnDefault("true")
    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "quantity_stock", nullable = false)
    private Integer quantityStock = 0;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "quantity_held", nullable = false)
    private Integer quantityHeld = 0;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "quantity_committed", nullable = false)
    private Integer quantityCommitted = 0;

    @Formula("(quantity_stock - quantity_held - quantity_committed)")
    private Integer realStock;

    @Column(name = "min_stock_level")
    private Integer minStockLevel;

    @Column(name = "replenishment_days")
    private Integer replenishmentDays; // Intervalo em dias

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @SQLRestriction("deleted = false")
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Integer getRealStock() {
        if (realStock != null)
            return realStock;

        int stock = quantityStock != null ? quantityStock : 0;
        int held = quantityHeld != null ? quantityHeld : 0;
        int committed = quantityCommitted != null ? quantityCommitted : 0;

        return stock - held - committed;
    }

    public void commitStock(Integer quantity) {
        if (!this.available)
            throw new ConflictException("Produto indiponível no momento: " + this.name);

        if (quantity == null || quantity <= 0)
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");

        if (this.getRealStock() < quantity)
            throw new IllegalStateException("Estoque insuficiente para o produto: " + this.name
                    + ". Estoque disponível: " + this.getRealStock());

        this.quantityCommitted += quantity;
    }
}
