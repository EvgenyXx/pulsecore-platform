package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.api.dto.SubscriptionRequest;
import ru.pulsecore.admin_service.service.AdminSubscriptionService;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.shared.dto.player.SubscriptionStatusResponse;

import java.util.UUID;

@Tag(name = "Управление подписками")
@AdminController
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final AdminSubscriptionService adminSubscriptionService;

    @Operation(summary = "Активировать подписку игроку")
    @PostMapping(AdminApi.SUBSCRIBE)
    public ResponseEntity<MessageResponse> subscribe(@RequestBody SubscriptionRequest request) {
        adminSubscriptionService.activateSubscription(request.playerId(), request.days());
        return ResponseEntity.ok(new MessageResponse("Подписка активирована на " + request.days() + " дней"));
    }

    @Operation(summary = "Отключить подписку игроку")
    @DeleteMapping(AdminApi.UNSUBSCRIBE)
    public ResponseEntity<MessageResponse> unsubscribe(@PathVariable UUID playerId) {
        adminSubscriptionService.deactivateSubscription(playerId);
        return ResponseEntity.ok(new MessageResponse("Подписка отключена"));
    }

    @Operation(summary = "Получить статус подписки игрока")
    @GetMapping(AdminApi.PLAYER_SUBSCRIPTION)
    public ResponseEntity<SubscriptionStatusResponse> getSubscription(@PathVariable UUID playerId) {
        return ResponseEntity.ok(adminSubscriptionService.getSubscriptionStatus(playerId));
    }
}