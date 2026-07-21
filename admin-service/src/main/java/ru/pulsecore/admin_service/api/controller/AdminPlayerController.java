package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.service.AdminPlayerService;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Управление игроками")
@AdminController
@RequiredArgsConstructor
public class AdminPlayerController {

    private final AdminPlayerService adminPlayerService;

    @Operation(summary = "Удалить игрока")
    @DeleteMapping(AdminApi.DELETE_PLAYER)
    public ResponseEntity<MessageResponse> deletePlayer(@PathVariable UUID playerId) {
        return ResponseEntity.ok(adminPlayerService.deleteAccount(playerId));
    }

    @Operation(summary = "Поиск игроков")
    @GetMapping(AdminApi.SEARCH)
    public ResponseEntity<List<PlayerSearchResponse>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(adminPlayerService.search(query));
    }
}