package ru.pulsecore.tournaments.persistence.repository.projection;

import java.time.LocalDate;

public interface LastResultProjection {
    LocalDate getDate();
    Double getAmount();
    String getTournamentLink();
}