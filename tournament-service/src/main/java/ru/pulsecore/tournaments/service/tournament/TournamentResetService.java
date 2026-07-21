
package ru.pulsecore.tournaments.service.tournament;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.tournaments.persistence.repository.TournamentResultRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentResetService {

    private final TournamentResultRepository tournamentResultRepository;

    private final TournamentAutoAddService tournamentAutoAddService;
    private final TournamentCascadeSyncService cascadeSyncService;

    @Transactional
    public int deleteAllTournaments(UUID playerId) {
        return tournamentResultRepository.deleteByPlayerId(playerId);
    }

    public void resyncAll(UUID playerId,String playerName) {

        tournamentAutoAddService.addRecentTournamentsForPlayer(playerId, playerName,30);
        cascadeSyncService.syncAllHistory(playerId, playerName);
    }
}