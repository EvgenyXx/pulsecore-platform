package ru.pulsecore.user_service.api.controller.internal;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;

import ru.pulsecore.user_service.service.player.PlayerService;

import java.util.UUID;

@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
@RestController
public class InternalPlayerController {

    private final PlayerService playerService;

    @DeleteMapping(FeignPlayerApi.DELETE)
    public ResponseEntity<Void> deleteInternalPlayer(@PathVariable UUID playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.noContent().build();
    }
}
