package ru.pulsecore.shared.dto.player;

public record PlayerSettingsDto(
        boolean pushEnabled,
        boolean notificationsEnabled,
        boolean hasActiveSubscription) {}