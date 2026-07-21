package ru.pulsecore.tournaments.service.tournament;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.service.application.TournamentResultService;





@Service
@RequiredArgsConstructor
public class TournamentFacade {


    private final TournamentResultService tournamentResultService;


    public void updateResult(Long tournamentId, Double amount, Double bonus) {
        tournamentResultService.updateResult(tournamentId, amount, bonus);
    }


}