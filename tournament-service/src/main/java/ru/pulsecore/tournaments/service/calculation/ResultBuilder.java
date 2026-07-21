package ru.pulsecore.tournaments.service.calculation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.tournaments.api.dto.ResultDto;
import ru.pulsecore.tournaments.domain.MatchProcessingResult;
import ru.pulsecore.tournaments.domain.TournamentContext;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResultBuilder {

    private final BonusCalculator bonusCalculator;

    public List<ResultDto> build(MatchProcessingResult matchResult,
                                 TournamentContext ctx) {

        List<ResultDto> results = new ArrayList<>();

        for (String player : matchResult.getPointsMap().keySet()) {

            int place = matchResult.getPlaceMap().getOrDefault(player, 0);
            int bonus = bonusCalculator.getBonus(place);

            int base = matchResult.getPointsMap().get(player) + bonus;
            int total = base + (int) ctx.getNightBonus();

            results.add(new ResultDto(
                    null,  // id — будет проставлен после сохранения
                    player,
                    place,
                    bonus,
                    total,
                    ctx.getDate()
            ));
        }

        return results;
    }
}