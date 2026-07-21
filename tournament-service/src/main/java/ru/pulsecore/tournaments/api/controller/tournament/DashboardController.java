package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.tournament.response.LastResultDto;
import ru.pulsecore.shared.dto.tournament.response.UpcomingLineupDto;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.service.internal.DashboardService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Дашборд")
@ApiV1TournamentController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Последний результат игрока")
    @GetMapping(TournamentApi.LAST_RESULT)
    public LastResultDto getLastResult(@AuthenticationPrincipal Jwt jwt) {
        return dashboardService.getLastResult(UUID.fromString(jwt.getSubject()));
    }

    @Operation(summary = "Ближайшие составы с участием игрока на главной странице")
    @GetMapping(TournamentApi.UPCOMING_LINEUPS)
    public List<UpcomingLineupDto> getUpcomingLineups(@RequestParam String playerName) {
        return dashboardService.getUpcomingLineups(playerName);
    }
}