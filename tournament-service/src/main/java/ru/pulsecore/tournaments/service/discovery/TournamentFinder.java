package ru.pulsecore.tournaments.service.discovery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.tournaments.service.application.UpcomingTournamentService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TournamentFinder {

    private final UpcomingTournamentService tournamentService;

    public Map<String, List<TournamentDto>> find(Set<String> playerNames) {
        return tournamentService.findPlayersTournaments(playerNames);
    }
}