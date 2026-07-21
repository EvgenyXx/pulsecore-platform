package ru.pulsecore.tournaments.service.calculation.strategy;

import ru.pulsecore.tournaments.domain.MatchProcessingResult;
import ru.pulsecore.tournaments.domain.StrategyType;
import ru.pulsecore.tournaments.domain.TournamentContext;

public interface MatchCalculationStrategy {

    StrategyType getType() ;

    MatchProcessingResult process(TournamentContext ctx);
}