package ru.pulsecore.user_service.repository.projection;

import java.util.UUID;

public interface PlayerSettingsProjection {
    UUID getId();
    boolean getPushEnabled();
    boolean getNotificationsEnabled();
    boolean getActiveSubscription();
}