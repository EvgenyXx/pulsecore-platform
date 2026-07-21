package ru.pulsecore.shared.dto.player;

import java.util.UUID;

public record BroadcastRecipient(UUID id, String email, boolean pushEnabled) {}