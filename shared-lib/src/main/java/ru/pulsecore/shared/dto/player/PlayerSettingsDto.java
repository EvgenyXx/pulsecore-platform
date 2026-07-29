package ru.pulsecore.shared.dto.player;

import java.util.UUID;

public record PlayerSettingsDto(
        UUID playerId,
        boolean pushEnabled,
        boolean notificationsEnabled,
        boolean hasActiveSubscription) {}