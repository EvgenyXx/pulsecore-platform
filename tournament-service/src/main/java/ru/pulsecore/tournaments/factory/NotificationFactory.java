package ru.pulsecore.tournaments.factory;
import org.springframework.stereotype.Component;

import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import java.util.UUID;

@Component
public class NotificationFactory {

    public PlayerNotification create(UUID playerId, TournamentEntity tournament, TournamentDto t) {
        PlayerNotification pn = new PlayerNotification();

        pn.setPlayerId(playerId);
        pn.setTournament(tournament);
        pn.setHall(t.getHallNumber());

        return pn;
    }
}