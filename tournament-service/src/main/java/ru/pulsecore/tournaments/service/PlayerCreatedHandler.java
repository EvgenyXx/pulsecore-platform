package ru.pulsecore.tournaments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.tournaments.service.tournament.TournamentAutoAddService;
import ru.pulsecore.tournaments.service.tournament.TournamentCascadeSyncService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerCreatedHandler {
    private final TournamentAutoAddService tournamentAutoAddService;
    private final TournamentCascadeSyncService cascadeSyncService;

    public void handle(PlayerCreatedEvent event) {
        tournamentAutoAddService.addRecentTournamentsForPlayer(
                event.playerId(), event.playerName(), event.days());
        cascadeSyncService.syncAllHistory(event.playerId(), event.playerName());
        log.info("Player {} created", event.playerId());
    }
}