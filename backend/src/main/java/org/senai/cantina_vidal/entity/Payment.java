package org.senai.cantina_vidal.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.senai.cantina_vidal.enums.PaymentMethod;
import org.senai.cantina_vidal.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "payments")
public class Payment extends Auditable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 50)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private PaymentStatus status;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "qr_code_payload", length = Integer.MAX_VALUE)
    private String qrCodePayload;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
