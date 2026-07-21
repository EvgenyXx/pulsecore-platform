package ru.pulsecore.shared.dto.event;

import java.util.UUID;

public record PlayerCreatedEvent(UUID playerId, String playerName, String email, int days) {}