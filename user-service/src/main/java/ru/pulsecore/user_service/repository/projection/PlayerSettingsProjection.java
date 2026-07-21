package ru.pulsecore.user_service.repository.projection;

public interface PlayerSettingsProjection {
    boolean getPushEnabled();
    boolean getNotificationsEnabled();
    boolean getActiveSubscription();
}