package ru.pulsecore.admin_service.api.dto;

import java.util.UUID;

public record SubscriptionRequest(UUID playerId, int days) {}