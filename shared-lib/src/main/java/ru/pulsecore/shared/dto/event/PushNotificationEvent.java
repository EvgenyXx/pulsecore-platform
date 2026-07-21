package ru.pulsecore.shared.dto.event;

import java.util.UUID;

public record PushNotificationEvent(
        UUID playerId,
        String title,
        String body,
        String url
) {
}
