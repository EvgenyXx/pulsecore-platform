package ru.pulsecore.user_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.user_service.service.player.PlayerService;

import java.util.List;

@Tag(name = "Игроки (internal)")
@RestController
@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
public class InternalPlayerListController {

    private final PlayerService playerService;

    @Operation(summary = "Получить всех активных игроков")
    @GetMapping(FeignPlayerApi.ALL_ACTIVE)
    public List<PlayerData> getAllActivePlayers() {
        return playerService.getAll().stream()
                .map(p -> new PlayerData(p.getId(), p.getName(), p.getEmail()))
                .toList();
    }
}