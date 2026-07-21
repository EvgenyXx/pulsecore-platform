
package ru.pulsecore.tournaments.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignTournamentApi;
import ru.pulsecore.shared.dto.tournament.request.CalculateRequest;
import ru.pulsecore.shared.dto.tournament.response.AdminCalculateResponse;
import ru.pulsecore.tournaments.service.application.AdminCalculateService;
import ru.pulsecore.tournaments.service.tournament.TournamentResetService;

import java.util.UUID;

@Tag(name = "Администрирование (internal)")
@RestController
@RequestMapping(FeignTournamentApi.BASE)
@RequiredArgsConstructor
public class InternalAdminTournamentController {

    private final TournamentResetService tournamentResetService;
    private final AdminCalculateService adminCalculateService;

    @Operation(summary = "Удалить все турниры игрока")
    @DeleteMapping(FeignTournamentApi.PLAYER_TOURNAMENTS)
    public ResponseEntity<Integer> deletePlayerTournaments(@PathVariable UUID playerId) {
        return ResponseEntity.ok(tournamentResetService.deleteAllTournaments(playerId));
    }

    @Operation(summary = "Перезаписать все  турниры игрока")
    @PostMapping(FeignTournamentApi.PLAYER_TOURNAMENTS_RESYNC)
    public ResponseEntity<Void> resyncPlayerTournaments(@PathVariable UUID playerId,
                                                        @RequestParam String playerName) {
        tournamentResetService.resyncAll(playerId, playerName);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Посчитать любого игрока")
    @PostMapping(FeignTournamentApi.CALCULATE)
    public ResponseEntity<AdminCalculateResponse> calculate(@RequestBody CalculateRequest request) {
        return ResponseEntity.ok(adminCalculateService.calculate(
                request.name(), request.startDate(), request.endDate()));
    }
}