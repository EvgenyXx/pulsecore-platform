package ru.pulsecore.user_service.api.controller.player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import ru.pulsecore.shared.dto.player.SubscriptionStatusResponse;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.service.subscription.SubscriptionFacade;

import java.util.UUID;

@Tag(name = "Подписка")
@ApiV1PlayerController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionFacade subscriptionFacade;

    @Operation(summary = "Получить статус подписки")
    @GetMapping(PlayerApi.SUBSCRIPTION)
    public ResponseEntity<SubscriptionStatusResponse> getSubscription(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(subscriptionFacade.getSubscription(UUID.fromString(jwt.getSubject())));
    }
}