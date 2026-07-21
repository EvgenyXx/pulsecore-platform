package ru.pulsecore.tournaments.persistence.repository.projection;

public interface LeagueStatProjection {
    String getLeague();
    Long getCount();
    Double getSum();
    Double getAvg();
}
