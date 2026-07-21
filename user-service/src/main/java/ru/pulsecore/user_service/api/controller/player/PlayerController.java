package ru.pulsecore.user_service.api.controller.player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.user_service.api.dto.request.HallsRequest;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.api.dto.request.ChangePasswordRequest;
import ru.pulsecore.user_service.api.dto.request.UpdateProfileRequest;
import ru.pulsecore.user_service.api.dto.response.NotificationsStatusResponse;
import ru.pulsecore.user_service.api.dto.response.PlayerProfileResponse;
import ru.pulsecore.user_service.api.dto.response.PushStatusResponse;
import ru.pulsecore.user_service.service.player.PlayerFacade;


import java.util.UUID;


@Tag(name = "Управление игроком")
@ApiV1PlayerController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerFacade playerFacade;

    @Operation(summary = "Получить статус push-уведомлений")
    @GetMapping(PlayerApi.PUSH_STATUS)
    public ResponseEntity<PushStatusResponse> pushStatus(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(new PushStatusResponse(playerFacade.isPushEnabled(uid(jwt))));
    }

    @Operation(summary = "Включить/выключить push-уведомления")
    @PostMapping(PlayerApi.PUSH_TOGGLE)
    public ResponseEntity<PushStatusResponse> togglePush(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(new PushStatusResponse(playerFacade.togglePushEnabled(uid(jwt))));
    }

    @Operation(summary = "Обновить профиль игрока")
    @PutMapping(PlayerApi.PROFILE)
    public ResponseEntity<PlayerProfileResponse> updateProfile(@AuthenticationPrincipal Jwt jwt,
                                                               @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(playerFacade.updateProfile(uid(jwt), request));
    }

    @Operation(summary = "Сменить пароль")
    @PutMapping(PlayerApi.CHANGE_PASSWORD)
    public ResponseEntity<MessageResponse> changePassword(@AuthenticationPrincipal Jwt jwt,
                                                          @Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(playerFacade.changePassword(uid(jwt), request));
    }

    @Operation(summary = "Включить/выключить уведомления")
    @PutMapping(PlayerApi.NOTIFICATIONS)
    public ResponseEntity<MessageResponse> toggleNotifications(@AuthenticationPrincipal Jwt jwt,
                                                               @RequestParam boolean enabled) {
        return ResponseEntity.ok(playerFacade.toggleNotifications(uid(jwt), enabled));
    }

    @Operation(summary = "Получить статус уведомлений")
    @GetMapping(PlayerApi.NOTIFICATIONS_STATUS)
    public ResponseEntity<NotificationsStatusResponse> getNotificationsStatus(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(playerFacade.getNotificationsStatus(uid(jwt)));
    }

    @Operation(summary = "Сохранить выбранные залы")
    @PutMapping(PlayerApi.HALLS)
    public ResponseEntity<Void> saveHalls(@AuthenticationPrincipal Jwt jwt,
                                          @RequestBody HallsRequest request) {
        playerFacade.saveSelectedHalls(uid(jwt), request.halls());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Сохранить залы для live-трансляций")
    @PutMapping(PlayerApi.LIVE_HALLS)
    public ResponseEntity<Void> saveLiveHalls(@AuthenticationPrincipal Jwt jwt,
                                              @RequestBody HallsRequest request) {
        playerFacade.saveLiveSelectedHalls(uid(jwt), request.halls());
        return ResponseEntity.ok().build();
    }

    private UUID uid(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}