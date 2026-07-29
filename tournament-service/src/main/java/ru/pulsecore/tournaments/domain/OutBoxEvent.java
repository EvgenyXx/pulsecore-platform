package ru.pulsecore.tournaments.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_tournament")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutBoxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Builder.Default
    @Column(nullable = false)
    private boolean sent = false;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime sentAt;

    @Builder.Default
    @Column(nullable = false)
    private int retryCount = 0;



}
