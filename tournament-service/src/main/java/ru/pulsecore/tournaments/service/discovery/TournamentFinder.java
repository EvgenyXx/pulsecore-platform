package ru.pulsecore.tournaments.service.discovery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.tournaments.service.application.UpcomingTournamentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentFinder {

    private final UpcomingTournamentService tournamentService;

    public List<TournamentDto> find(String playerName) {
        return tournamentService.findPlayerTournaments(playerName);
    }
}