package ru.pulsecore.user_service.repository.projection;

import java.time.LocalDateTime;

public interface SubscriptionProjection {
    boolean getActive();
    LocalDateTime getExpiresAt();
    LocalDateTime getStartedAt();
}