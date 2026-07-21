package ru.pulsecore.tournaments.domain;

import ru.pulsecore.tournaments.api.dto.ResultDto;

import java.util.List;

public record ParsedResult(Long tournamentId, List<ResultDto> results, TournamentStatus status,
                           double nightBonus,
                           boolean hasRemoved,
                           boolean isFinalRemoved,
                           String league,
                           String time) {

    public boolean isFinished() {
        return status != null && status.isFinished();
    }
}