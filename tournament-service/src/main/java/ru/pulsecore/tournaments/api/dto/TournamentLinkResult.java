package ru.pulsecore.tournaments.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.pulsecore.tournaments.domain.ParsedResult;
import ru.pulsecore.tournaments.persistence.entity.TournamentLinkStatus;

@Data
@AllArgsConstructor
public class TournamentLinkResult {

    private TournamentLinkStatus status;
    private ParsedResult parsed;

}