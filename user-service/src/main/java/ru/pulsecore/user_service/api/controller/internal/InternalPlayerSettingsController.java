package ru.pulsecore.user_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;
import ru.pulsecore.shared.dto.player.BroadcastRecipient;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.shared.dto.player.PlayerSettingsDto;
import ru.pulsecore.user_service.service.internal.InternalPlayerService;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Tag(name = "Игроки (internal)")
@RestController
@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
public class InternalPlayerSettingsController {

    private final InternalPlayerService internalPlayerService;

    @Operation(summary = "Получить имя игрока по ID")
    @GetMapping(FeignPlayerApi.GET_NAME_BY_ID)
    public String getPlayerName(@PathVariable UUID playerId) {
        return internalPlayerService.getPlayerName(playerId);
    }

    @Operation(summary = "Получить получателей для рассылки")
    @GetMapping(FeignPlayerApi.BROADCAST_RECIPIENTS)
    public List<BroadcastRecipient> getBroadcastRecipients() {
        return internalPlayerService.getBroadcastRecipients();
    }

    @Operation(summary = "Получить настройки игроков")
    @GetMapping(FeignPlayerApi.SETTINGS)
    public List<PlayerSettingsDto> getSettings(@RequestParam Set<UUID> playerId) {
        return internalPlayerService.getSettings(playerId);
    }

    @Operation(summary = "Получить игрока по ID")
    @GetMapping(FeignPlayerApi.GET_PLAYER_DATA_BY_IDS)
    public List<PlayerData> getById(@RequestParam Set<UUID> playerIds) {
        return internalPlayerService.getPlayerDataByIds(playerIds);
    }

    @Operation(summary = "Поиск игроков по имени")
    @GetMapping(FeignPlayerApi.SEARCH)
    public List<PlayerSearchResponse> searchByName(@RequestParam String q) {
        return internalPlayerService.searchByName(q);
    }

    @Operation(summary = "Найти ID игрока по полному имени")
    @GetMapping(FeignPlayerApi.ID_BY_NAME)
    public UUID getIdByFullName(@PathVariable String name) {
        return internalPlayerService.findIdByFullName(name);
    }
}