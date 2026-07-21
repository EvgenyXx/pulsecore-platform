package ru.pulsecore.tournaments.service.analytic;

import org.springframework.stereotype.Component;

import ru.pulsecore.shared.dto.analytics.AnalyticsResponse;
import ru.pulsecore.shared.dto.analytics.MonthlyIncomeResponse;
import ru.pulsecore.tournaments.persistence.repository.projection.LeagueStatProjection;
import ru.pulsecore.tournaments.persistence.repository.projection.MonthlyIncomeProjection;

import java.util.List;

@Component
public class AnalyticsMapper {

    public List<AnalyticsResponse.LeagueStat> toLeagueStats(List<LeagueStatProjection> projections) {
        return projections.stream()
                .map(p -> AnalyticsResponse.LeagueStat.builder()
                        .league(p.getLeague())
                        .tournamentCount(p.getCount().intValue())
                        .totalAmount(p.getSum())
                        .averageAmount(p.getAvg())
                        .build())
                .toList();
    }

    public List<MonthlyIncomeResponse.MonthStat> toMonthStats(List<MonthlyIncomeProjection> projections) {
        return projections.stream()
                .map(p -> MonthlyIncomeResponse.MonthStat.builder()
                        .month(p.getMonth())
                        .total(p.getTotal())
                        .count(p.getCount().intValue())
                        .average(p.getAverage())
                        .build())
                .toList();
    }
}