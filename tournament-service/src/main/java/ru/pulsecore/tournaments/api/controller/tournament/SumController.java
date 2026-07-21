package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.api.dto.request.InternalSumRequest;
import ru.pulsecore.tournaments.service.internal.SumService;

import java.util.UUID;

@Tag(name = "Сумма")
@ApiV1TournamentController
@RequiredArgsConstructor
public class SumController {

    private final SumService sumService;

    @Operation(summary = "Подсчитать сумму заработка за выбранный период")
    @GetMapping(TournamentApi.SUM)
    public SumResponse getSum(@AuthenticationPrincipal Jwt jwt, @RequestBody InternalSumRequest request) {
        return sumService.getSum(
                UUID.fromString(jwt.getSubject()),
                request.start(), request.end(),
                request.page(), request.size());
    }
}