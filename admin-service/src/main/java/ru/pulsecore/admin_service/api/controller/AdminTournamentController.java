package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.service.AdminTournamentService;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.shared.dto.tournament.request.CalculateRequest;
import ru.pulsecore.shared.dto.tournament.response.AdminCalculateResponse;

import java.util.UUID;

@Tag(name = "Управление турнирами")
@AdminController
@RequiredArgsConstructor
public class AdminTournamentController {

    private final AdminTournamentService tournamentFacade;

    @Operation(summary = "Посчитать любого игрока")
    @PostMapping(AdminApi.TOURNAMENT_CALCULATE)
    public ResponseEntity<AdminCalculateResponse> calculate(@RequestBody CalculateRequest request) {
        return ResponseEntity.ok(tournamentFacade.calculate(request));
    }

    @Operation(summary = "Удалить все турниры игрока")
    @DeleteMapping(AdminApi.PLAYER_TOURNAMENTS)
    public ResponseEntity<MessageResponse> deletePlayerTournaments(@PathVariable UUID playerId) {
        int deleted = tournamentFacade.deletePlayerTournaments(playerId);
        return ResponseEntity.ok(new MessageResponse("Удалено турниров: " + deleted));
    }

    @Operation(summary = "Пересинхронизировать турниры игрока")
    @PostMapping(AdminApi.PLAYER_TOURNAMENTS_RESYNC)
    public ResponseEntity<MessageResponse> resyncPlayerTournaments(@PathVariable UUID playerId) {
        tournamentFacade.resyncPlayerTournaments(playerId);
        return ResponseEntity.ok(new MessageResponse("Загрузка турниров запущена в фоне"));
    }
}