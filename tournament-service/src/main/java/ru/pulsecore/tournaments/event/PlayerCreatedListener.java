package ru.pulsecore.tournaments.event;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.tournaments.service.tournament.TournamentAutoAddService;
import ru.pulsecore.tournaments.service.tournament.TournamentCascadeSyncService;

@Component
@RequiredArgsConstructor
public class PlayerCreatedListener {

    private final TournamentAutoAddService tournamentAutoAddService;
    private final TournamentCascadeSyncService cascadeSyncService;

    @KafkaListener(topics = KafkaTopics.PLAYER_CREATED)
    public void handle(PlayerCreatedEvent event) {
        System.out.println("ПОЛУЧЕН ИВЕНТ КАФКА " + event);
        tournamentAutoAddService.addRecentTournamentsForPlayer(
                event.playerId(), event.playerName(), event.days());

        cascadeSyncService.syncAllHistory(event.playerId(),event.playerName());
    }

}