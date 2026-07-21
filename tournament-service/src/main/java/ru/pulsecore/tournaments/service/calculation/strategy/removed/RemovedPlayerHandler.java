package ru.pulsecore.tournaments.service.calculation.strategy.removed;

import ru.pulsecore.tournaments.domain.MatchProcessingResult;
import ru.pulsecore.tournaments.domain.RemovedStage;
import ru.pulsecore.tournaments.domain.TournamentContext;

public interface RemovedPlayerHandler {

    RemovedStage getStage();

    MatchProcessingResult handle(TournamentContext ctx);
}