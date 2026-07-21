package ru.pulsecore.tournaments.persistence.repository.projection;

public interface MonthlyIncomeProjection {
    String getMonth();
    Double getTotal();
    Long getCount();
    Double getAverage();
}