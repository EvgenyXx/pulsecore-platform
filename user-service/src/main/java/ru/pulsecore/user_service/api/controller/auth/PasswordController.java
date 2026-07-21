package ru.pulsecore.user_service.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.api.dto.request.ForgotPasswordRequest;
import ru.pulsecore.user_service.api.dto.request.ResetPasswordRequest;
import ru.pulsecore.user_service.api.dto.request.VerifyPasswordRequest;
import ru.pulsecore.user_service.service.auth.PlayerPasswordResetService;
import ru.pulsecore.user_service.service.player.PlayerProfileService;

import java.util.UUID;

@Tag(name = "Авторизация")
@ApiV1PlayerController
@RequiredArgsConstructor
public class PasswordController {

    private final PlayerPasswordResetService passwordResetService;
    private final PlayerProfileService playerProfileService;

    @Operation(summary = "Отправить код для сброса пароля")
    @PostMapping(PlayerApi.FORGOT_PASSWORD)
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.initiate(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("ok"));
    }

    @Operation(summary = "Сбросить пароль по коду")
    @PostMapping(PlayerApi.RESET_PASSWORD)
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.complete(request.getEmail(), request.getCode(), request.getPassword());
        return ResponseEntity.ok(new MessageResponse("ok"));
    }

    @Operation(summary = "Проверить текущий пароль")
    @PostMapping(PlayerApi.VERIFY_PASSWORD)
    public ResponseEntity<MessageResponse> verifyPassword(@AuthenticationPrincipal Jwt jwt,
                                                          @Valid @RequestBody VerifyPasswordRequest request) {
        playerProfileService.verifyPassword(UUID.fromString(jwt.getSubject()), request.getPassword());
        return ResponseEntity.ok(new MessageResponse("ok"));
    }
}