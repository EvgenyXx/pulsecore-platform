// PointsCalculator.java
package ru.pulsecore.tournaments.service.calculation;

import ru.pulsecore.tournaments.domain.Match;
import java.time.LocalDate;

public interface PointsCalculator {
    int calculatePoints(Match match, LocalDate tournamentDate);
}