package ru.pulsecore.shared.dto.tournament.response;

import java.util.UUID;

public record PriorityLeagueResponse(
        UUID playerId,
        String priorityLeague
) {
}
