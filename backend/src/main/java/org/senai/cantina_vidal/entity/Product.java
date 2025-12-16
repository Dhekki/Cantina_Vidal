package org.senai.cantina_vidal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Table(name = "products")
public class Product extends UserDateAudit {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Builder.Default
    @ColumnDefault("0.0")
    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice = BigDecimal.valueOf(0.0);

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @NotNull
    @Builder.Default
    @ColumnDefault("true")
    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @NotNull
    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "quantity_stock", nullable = false)
    private Integer quantityStock = 0;

    @NotNull
    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "quantity_held", nullable = false)
    private Integer quantityHeld = 0;

    @NotNull
    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "quantity_committed", nullable = false)
    private Integer quantityCommitted = 0;

    @Column(name = "min_stock_level")
    private Integer minStockLevel;

    @Column(name = "replenishment_days")
    private Integer replenishmentDays; // Intervalo em dias

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
}