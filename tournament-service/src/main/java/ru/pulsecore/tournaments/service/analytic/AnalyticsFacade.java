package ru.pulsecore.tournaments.service.analytic;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.config.CacheNames;
import ru.pulsecore.shared.dto.analytics.AnalyticsResponse;
import ru.pulsecore.shared.dto.analytics.BestTimeResponse;
import ru.pulsecore.shared.dto.analytics.DailyIncomeResponse;
import ru.pulsecore.shared.dto.analytics.MonthlyIncomeResponse;
import ru.pulsecore.tournaments.persistence.repository.PlayerAnalyticsRepository;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsFacade {


    private final LeagueAnalyticsService leagueAnalyticsService;
    private final PlayerIncomeService playerIncomeService;
    private final PlayerAnalyticsRepository playerAnalyticsRepository;

    @Cacheable(value = CacheNames.ANALYTICS, key = CacheNames.KEY_PLAYER_ID + " + ':' + #days")
    public AnalyticsResponse getAnalytics(UUID playerId, int days) {

        return leagueAnalyticsService.getAnalytics(playerId, days);
    }

    @Cacheable(value = CacheNames.MONTHLY_INCOME, key = CacheNames.KEY_PLAYER_ID + " + ':' + #year")
    public MonthlyIncomeResponse getMonthlyIncome(UUID playerId, int year) {

        return playerIncomeService.getMonthlyIncome(playerId, year);
    }

    @Cacheable(value = CacheNames.DAILY_INCOME, key = CacheNames.KEY_PLAYER_ID + " + ':' + #year + ':' + #month")
    public DailyIncomeResponse getDailyIncome(UUID playerId, int year, int month) {

        return playerIncomeService.getDailyIncome(playerId, year, month);
    }

    @Cacheable(value = CacheNames.BEST_TIME, key = CacheNames.KEY_PLAYER_ID + " + ':' + #start + ':' + #end")
    public List<BestTimeResponse> getBestTime(UUID playerId, LocalDate start, LocalDate end) {
        return playerAnalyticsRepository.getBestTime(playerId, start, end);
    }
}