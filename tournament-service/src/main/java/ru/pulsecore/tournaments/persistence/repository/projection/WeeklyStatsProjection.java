package ru.pulsecore.tournaments.persistence.repository.projection;

;

public interface WeeklyStatsProjection {
    String getName();
    Long getTournaments();
    Double getTotal();
    Double getAverage();
}