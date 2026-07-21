package ru.pulsecore.user_service.repository.projection;

import java.util.UUID;

public interface BroadcastProjection {
    UUID getId();
    String getEmail();
    boolean getPushEnabled();
}