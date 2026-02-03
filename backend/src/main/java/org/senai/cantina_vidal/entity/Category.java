package org.senai.cantina_vidal.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.ColumnDefault;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id = ?")
@Table(name = "categories")
public class Category extends Auditable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Size(max = 7)
    @Column(name = "color_hex", length = 7)
    private String colorHex;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}