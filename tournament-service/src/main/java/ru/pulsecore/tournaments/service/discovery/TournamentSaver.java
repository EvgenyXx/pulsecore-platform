package ru.pulsecore.tournaments.service.discovery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.factory.NotificationFactory;
import ru.pulsecore.tournaments.factory.TournamentFactory;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentSaver {

    private final TournamentRepository tournamentRepository;
    private final NotificationFactory notificationFactory;
    private final PlayerNotificationRepository notificationRepo;
    private final TournamentFactory tournamentFactory;

    public void save(UUID playerId, List<TournamentDto> tournaments) {

        for (TournamentDto t : tournaments) {

            TournamentEntity tournament = tournamentRepository
                    .findByExternalId(t.getId())
                    .orElseGet(() ->
                            tournamentRepository.save(
                                    tournamentFactory.create(t)
                            )
                    );

            PlayerNotification pn = notificationFactory.create(playerId, tournament, t);

            notificationRepo.save(pn);
        }
    }
}