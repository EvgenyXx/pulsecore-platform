package ru.pulsecore.user_service.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.api.dto.request.LoginRequest;
import ru.pulsecore.user_service.api.dto.response.AuthResponse;
import ru.pulsecore.user_service.api.dto.response.MeResponse;
import ru.pulsecore.user_service.api.dto.response.RefreshResponse;
import ru.pulsecore.user_service.service.auth.AuthFacade;

import java.util.Map;

@Tag(name = "Авторизация")
@ApiV1PlayerController
@RequiredArgsConstructor
public class LoginController {

    private final AuthFacade authFacade;

    @Operation(summary = "Войти в систему")
    @PostMapping(PlayerApi.LOGIN)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        return ResponseEntity.ok(authFacade.login(request.getEmail(), request.getPassword(), response));
    }

    @Operation(summary = "Обновить access-токен")
    @PostMapping(PlayerApi.REFRESH)
    public ResponseEntity<RefreshResponse> refresh(@CookieValue("refresh_token") String refreshToken,
                                                   HttpServletResponse response) {
        return ResponseEntity.ok(authFacade.refresh(refreshToken, response));
    }

    @Operation(summary = "Получить профиль текущего игрока")
    @GetMapping(PlayerApi.ME)
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(authFacade.me(jwt.getSubject()));
    }

    @Operation(summary = "Сменить тему оформления")
    @PostMapping(PlayerApi.ME_THEME)
    public ResponseEntity<Void> setTheme(@AuthenticationPrincipal Jwt jwt, @RequestBody Map<String, String> body) {
        if (jwt == null) return ResponseEntity.status(401).build();
        authFacade.setTheme(jwt.getSubject(), body.getOrDefault("theme", "dark"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Выйти из системы")
    @PostMapping(PlayerApi.LOGOUT)
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authFacade.logout(response);
        return ResponseEntity.ok().build();
    }
}