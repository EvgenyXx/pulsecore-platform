package ru.pulsecore.shared.dto.player;

import java.util.UUID;

public record PlayerSearchResponse(UUID id, String name, String email) {}