package ru.pulsecore.tournaments.service.calculation.strategy.removed.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.tournaments.service.calculation.strategy.DefaultMatchCalculationStrategy;
import ru.pulsecore.tournaments.service.calculation.strategy.removed.RemovedPlayerHandler;
import ru.pulsecore.tournaments.domain.RemovedStage;
import ru.pulsecore.tournaments.domain.MatchProcessingResult;
import ru.pulsecore.tournaments.domain.TournamentContext;

@Component
@RequiredArgsConstructor
public class RemovedInThirdPlaceHandler implements RemovedPlayerHandler {

    private final DefaultMatchCalculationStrategy defaultStrategy;

    @Override
    public RemovedStage getStage() {
        return RemovedStage.THIRD_PLACE;
    }

    @Override
    public MatchProcessingResult handle(TournamentContext ctx) {

        return defaultStrategy.process(ctx);
    }
}