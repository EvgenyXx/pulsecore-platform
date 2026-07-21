package ru.pulsecore.user_service.service.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.api.dto.response.AuthResponse;
import ru.pulsecore.user_service.api.dto.response.MeResponse;
import ru.pulsecore.user_service.api.dto.response.RefreshResponse;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.service.CookieService;
import ru.pulsecore.user_service.service.jwt.AuthTokenService;
import ru.pulsecore.user_service.service.theme.ThemeService;
import ru.pulsecore.user_service.service.player.PlayerService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final PlayerAuthenticationService authenticationService;
    private final PlayerService playerService;
    private final ThemeService themeService;
    private final AuthTokenService authTokenService;
    private final CookieService cookieService;

    public AuthResponse login(String email, String rawPassword, HttpServletResponse response) {
        Player player = authenticationService.authenticate(email, rawPassword);
        var tokens = authTokenService.generateTokens(player);
        cookieService.setRefreshTokenCookie(response, tokens.refreshToken());

        return AuthResponse.builder()
                .id(player.getId().toString())
                .name(player.getName())
                .email(player.getEmail())
                .accessToken(tokens.accessToken())
                .build();
    }

    public RefreshResponse refresh(String refreshToken, HttpServletResponse response) {
        var tokens = authTokenService.refreshToken(refreshToken);
        cookieService.setRefreshTokenCookie(response, tokens.refreshToken()); // новый в куку
        return new RefreshResponse(tokens.accessToken()); // возвращаем access
    }

    public void logout(HttpServletResponse response) {
        cookieService.deleteRefreshTokenCookie(response);
    }

    public MeResponse me(String playerId) {
        var player = playerService.findById(UUID.fromString(playerId));
        String theme = themeService.getTheme(UUID.fromString(playerId));
        return new MeResponse(playerId,
                player != null ? player.getName() : null,
                player != null ? player.getEmail() : null,
                player != null ? player.getCreatedAt() : null,
                player != null && player.isAdmin(),
                theme);
    }

    public void setTheme(String playerId, String theme) {
        themeService.setTheme(UUID.fromString(playerId), theme);
    }
}