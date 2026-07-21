package ru.pulsecore.user_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;
import ru.pulsecore.user_service.service.player.PlayerHallsService;

import java.util.UUID;

@Tag(name = "Залы (internal)")
@RestController
@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
public class InternalPlayerHallsController {

    private final PlayerHallsService playerHallsService;

    @Operation(summary = "Получить выбранные залы игрока")
    @GetMapping(FeignPlayerApi.GET_HALLS)
    public ResponseEntity<String> getSelectedHalls(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerHallsService.getSelectedHalls(playerId));
    }

    @Operation(summary = "Получить live-залы игрока")
    @GetMapping(FeignPlayerApi.GET_LIVE_HALLS)
    public ResponseEntity<String> getLiveSelectedHalls(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerHallsService.getLiveSelectedHalls(playerId));
    }
}