package ru.pulsecore.tournaments.persistence.repository.projection;

public interface WeekStatsProjection {
    String getPlayerName();
    Double getTotal();
    Long getTournaments();
}