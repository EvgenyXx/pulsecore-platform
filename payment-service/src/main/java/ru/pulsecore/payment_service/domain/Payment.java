package ru.pulsecore.payment_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer months;

    @Column(name = "payment_id", unique = true)
    private String paymentId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;



}