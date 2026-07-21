package ru.pulsecore.user_service.repository.projection;

import java.util.UUID;

public interface PlayerSearchProjection {
    UUID getId();
    String getName();
    String getEmail();
}