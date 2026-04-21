package org.senai.cantina_vidal.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.senai.cantina_vidal.enums.Role;

import jakarta.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Table(name = "users")
public class User extends Auditable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Role role;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "verification_code", length = 20)
    private String verificationCode;

    @Column(name = "verification_expires_at")
    private LocalDateTime verificationExpiresAt;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;
}
