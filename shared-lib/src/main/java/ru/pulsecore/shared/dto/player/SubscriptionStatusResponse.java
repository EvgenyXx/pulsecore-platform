package ru.pulsecore.shared.dto.player;

import lombok.Builder;

@Builder
public record SubscriptionStatusResponse(boolean active, String expiresAt, String startedAt) {}