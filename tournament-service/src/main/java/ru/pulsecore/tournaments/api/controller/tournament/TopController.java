package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.tournament.response.TopLeagueResponse;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.service.internal.TopPeriodService;

import java.util.UUID;

@Tag(name = "Зал славы")
@ApiV1TournamentController
@RequiredArgsConstructor
public class TopController {

    private final TopPeriodService topPeriodService;

    @Operation(summary = "Топ по всем лигам за период")
    @GetMapping(TournamentApi.TOP_ALL)
    public TopLeagueResponse getTopAll(@PathVariable String period,
                                       @AuthenticationPrincipal Jwt jwt) {
        return topPeriodService.getTopAllLeagues(period, UUID.fromString(jwt.getSubject()));
    }

    @Operation(summary = "Топ по конкретной лиге за период")
    @GetMapping(TournamentApi.TOP_BY_LEAGUE)
    public TopLeagueResponse getTopByLeague(@PathVariable String period,
                                            @PathVariable String league,
                                            @AuthenticationPrincipal Jwt jwt) {
        return topPeriodService.getTopByLeague(period, league, UUID.fromString(jwt.getSubject()));
    }
}