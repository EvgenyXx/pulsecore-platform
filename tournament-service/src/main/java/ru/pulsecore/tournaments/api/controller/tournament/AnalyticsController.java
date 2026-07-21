// AnalyticsController.java
package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.analytics.AnalyticsResponse;
import ru.pulsecore.shared.dto.analytics.BestTimeResponse;
import ru.pulsecore.shared.dto.analytics.DailyIncomeResponse;
import ru.pulsecore.shared.dto.analytics.MonthlyIncomeResponse;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.service.analytic.AnalyticsFacade;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Аналитика")
@ApiV1TournamentController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsFacade analyticsFacade;

    @Operation(summary = "Получить общую аналитику за N дней")
    @GetMapping(TournamentApi.ANALYTICS)
    public AnalyticsResponse getAnalytics(@AuthenticationPrincipal Jwt jwt, @RequestParam int days) {
        return analyticsFacade.getAnalytics(UUID.fromString(jwt.getSubject()), days);
    }

    @Operation(summary = "Доход по месяцам за год")
    @GetMapping(TournamentApi.MONTHLY_INCOME)
    public MonthlyIncomeResponse getMonthlyIncome(@AuthenticationPrincipal Jwt jwt, @RequestParam int year) {
        return analyticsFacade.getMonthlyIncome(UUID.fromString(jwt.getSubject()), year);
    }

    @Operation(summary = "Доход по дням за месяц")
    @GetMapping(TournamentApi.DAILY_INCOME)
    public DailyIncomeResponse getDailyIncome(@AuthenticationPrincipal Jwt jwt,
                                              @RequestParam int year,
                                              @RequestParam int month) {
        return analyticsFacade.getDailyIncome(UUID.fromString(jwt.getSubject()), year, month);
    }

    @Operation(summary = "Лучшее время за период")
    @GetMapping(TournamentApi.BEST_TIME)
    public List<BestTimeResponse> getBestTime(@AuthenticationPrincipal Jwt jwt,
                                              @RequestParam LocalDate start,
                                              @RequestParam LocalDate end) {
        return analyticsFacade.getBestTime(UUID.fromString(jwt.getSubject()), start, end);
    }
}