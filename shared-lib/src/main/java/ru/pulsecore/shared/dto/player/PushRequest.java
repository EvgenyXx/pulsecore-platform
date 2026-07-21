package ru.pulsecore.shared.dto.player;

import java.util.UUID;

public record PushRequest(UUID playerId, String title, String body,String url) {
}
