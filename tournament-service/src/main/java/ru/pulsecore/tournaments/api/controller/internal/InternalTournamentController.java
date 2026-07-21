package ru.pulsecore.tournaments.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignTournamentApi;
import ru.pulsecore.shared.dto.tournament.request.SumRequest;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;
import ru.pulsecore.tournaments.service.internal.SumService;

@Tag(name = "Турниры (internal)")
@RestController
@RequiredArgsConstructor
@RequestMapping(FeignTournamentApi.BASE)
public class InternalTournamentController {

    private final SumService sumService;

    @Operation(summary = "Получить сумму по игроку для отчёта")
    @PostMapping(FeignTournamentApi.SUM)
    public SumResponse getSumForReport(@RequestBody SumRequest sumRequest) {
        return sumService.getSum(
                sumRequest.playerId(), sumRequest.start(),
                sumRequest.end(), 0, Integer.MAX_VALUE);
    }
}