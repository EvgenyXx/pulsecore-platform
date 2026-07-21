package ru.pulsecore.user_service.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.Role;
import ru.pulsecore.user_service.exception.TokenExpiredException;
import ru.pulsecore.user_service.service.player.PlayerService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtService jwtService;
    private final PlayerService playerService;

    public TokenPair generateTokens(Player player) {
        List<String>roles = extractRoles(player);

        String accessToken = jwtService.generateAccessToken(
                player.getId(), player.getName(), player.getEmail(), roles);
        String refreshToken = jwtService.generateRefreshToken(player.getId());

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair refreshToken(String refreshToken) {
        String playerId = jwtService.extractSubject(refreshToken);
        Player player = playerService.findById(UUID.fromString(playerId));

        if (jwtService.isTokenExpired(refreshToken)) {
            throw new TokenExpiredException();
        }

        return generateTokens(player);
    }

    private List<String>extractRoles(Player player) {
        return player.getRoles().stream()
                .map(Role::getName)
                .toList();
    }

    public record TokenPair(String accessToken, String refreshToken) {}
}