package ru.pulsecore.tournaments.persistence.repository.projection;

import java.util.UUID;

public interface PrimaryLeagueProjection {
    UUID getPlayerId();
    String getLeague();
}