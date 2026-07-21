package ru.pulsecore.notification_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.notification_service.api.PushApi;
import ru.pulsecore.notification_service.api.annotation.ApiV1PushController;
import ru.pulsecore.notification_service.api.dto.PushSubscriptionRequest;
import ru.pulsecore.notification_service.service.push.PushFacade;

import java.util.UUID;

@Tag(name = "Push-уведомления")
@ApiV1PushController
@RequiredArgsConstructor
public class PushController {

    private final PushFacade pushFacade;

    @Operation(summary = "Проверить статус подписки на push")
    @GetMapping(PushApi.STATUS)
    public ResponseEntity<Boolean> status(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(pushFacade.isSubscribed(UUID.fromString(jwt.getSubject())));
    }

    @Operation(summary = "Получить VAPID публичный ключ")
    @GetMapping(PushApi.VAPID_PUBLIC_KEY)
    public ResponseEntity<String> getVapidPublicKey() {
        return ResponseEntity.ok(pushFacade.getVapidPublicKey());
    }

    @Operation(summary = "Подписаться на push-уведомления")
    @PostMapping(PushApi.SUBSCRIBE)
    public ResponseEntity<Void> subscribe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody PushSubscriptionRequest request) {
        pushFacade.subscribe(UUID.fromString(jwt.getSubject()), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отписаться от push-уведомлений")
    @PostMapping(PushApi.UNSUBSCRIBE)
    public ResponseEntity<Void> unsubscribe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody PushSubscriptionRequest request) {
        pushFacade.unsubscribe(UUID.fromString(jwt.getSubject()), request.endpoint());
        return ResponseEntity.ok().build();
    }
}