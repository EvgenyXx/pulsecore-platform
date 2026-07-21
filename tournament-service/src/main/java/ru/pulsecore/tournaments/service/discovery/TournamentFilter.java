package ru.pulsecore.tournaments.service.discovery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentFilter {

    private final PlayerNotificationRepository notificationRepo;

    public List<TournamentDto> findNew(UUID playerId, List<TournamentDto> tournaments) {

        return tournaments.stream()
                .filter(t -> !notificationRepo
                        .existsByPlayerIdAndTournament_ExternalId(playerId, t.getId()))
                .toList();
    }
}