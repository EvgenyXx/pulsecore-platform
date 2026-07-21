package ru.pulsecore.shared.dto.event.payment;

import java.util.UUID;

public record PaymentSucceededEvent(UUID playerId, int days) {
}
