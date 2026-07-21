package ru.pulsecore.tournaments.api.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.pulsecore.shared.dto.tournament.TournamentDto;

@Data
@Builder
public class TournamentSearchResult {
    private TournamentDto tournament;
    private boolean saved;
}