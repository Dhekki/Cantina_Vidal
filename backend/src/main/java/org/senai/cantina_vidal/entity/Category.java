package org.senai.cantina_vidal.entity;

import lombok.*;

import jakarta.persistence.*;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.ColumnDefault;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor @AllArgsConstructor
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id = ?")
@Table(name = "categories")
public class Category extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "color_hex", length = 7)
    private String colorHex;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}
