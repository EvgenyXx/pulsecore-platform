package ru.pulsecore.payment_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_settings")
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Builder
public class AppSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;
}