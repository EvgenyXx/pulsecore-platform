package ru.pulsecore.user_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;
import ru.pulsecore.shared.dto.player.SubscriptionStatusResponse;
import ru.pulsecore.user_service.service.subscription.SubscriptionService;

import java.util.UUID;

@Tag(name = "Подписки (internal)")
@RestController
@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
public class InternalSubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Активировать подписку")
    @PostMapping(FeignPlayerApi.ACTIVATE_SUBSCRIPTION)
    public ResponseEntity<Void> activate(@PathVariable UUID playerId, @RequestParam int days) {
        subscriptionService.activate(playerId, days);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Деактивировать подписку")
    @PostMapping(FeignPlayerApi.DEACTIVATE_SUBSCRIPTION)
    public ResponseEntity<Void> deactivate(@PathVariable UUID playerId) {
        subscriptionService.deactivate(playerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить статус подписки игрока")
    @GetMapping(FeignPlayerApi.GET_SUBSCRIPTION)
    public ResponseEntity<SubscriptionStatusResponse> getSubscription(@PathVariable UUID playerId) {
        return ResponseEntity.ok(subscriptionService.getByPlayerId(playerId));
    }
}