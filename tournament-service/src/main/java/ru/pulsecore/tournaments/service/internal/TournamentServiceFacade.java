package ru.pulsecore.tournaments.service.internal;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import ru.pulsecore.shared.dto.tournament.response.PriorityLeagueResponse;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;
import ru.pulsecore.tournaments.persistence.repository.TournamentResultRepository;
import ru.pulsecore.tournaments.service.tournament.SumService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentServiceFacade {

    private final SumService sumService;
    private final TournamentResultRepository tournamentResultRepository;


    public SumResponse getSumForReport(UUID playerId, LocalDate start, LocalDate end, int page, int size) {
        return sumService.getSum(playerId, start, end, page, size);
    }

    public List<PriorityLeagueResponse> updatePriorityLeague(Set<UUID> playerIds) {
        return tournamentResultRepository.findPrimaryLeagues(playerIds)
                .stream()
                .map(p->
                        new PriorityLeagueResponse(
                                p.getPlayerId(),p.getLeague()
                        )).toList();
    }
}
