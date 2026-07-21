package ru.pulsecore.notification_service.api.dto;

public record PushSubscriptionRequest(
        String endpoint,
        String p256dh,
        String auth
) {}