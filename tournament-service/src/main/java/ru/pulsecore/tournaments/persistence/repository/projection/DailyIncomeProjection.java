package ru.pulsecore.tournaments.persistence.repository.projection;

public interface DailyIncomeProjection {
    Integer getDay();
    Double getTotal();
    Integer getCount();
}