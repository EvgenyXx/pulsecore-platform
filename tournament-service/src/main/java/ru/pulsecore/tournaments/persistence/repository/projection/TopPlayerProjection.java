package ru.pulsecore.tournaments.persistence.repository.projection;

public interface TopPlayerProjection {
    String getName();
    Double getTotal();
    Long getTournaments();
}