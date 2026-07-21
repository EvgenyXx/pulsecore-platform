package ru.pulsecore.user_service.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.exception.player.BadCredentialsException;
import ru.pulsecore.user_service.exception.player.OAuthOnlyLoginException;
import ru.pulsecore.user_service.repository.PlayerRepository;


@Service
@RequiredArgsConstructor
public class PlayerAuthenticationService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public Player authenticate(String email, String rawPassword) {
        String normalizedEmail = email.toLowerCase().trim();
        Player player = playerRepository.findByEmail(normalizedEmail)
                .orElseThrow(BadCredentialsException::new);

        checkOAuthOnly(player);
        checkPassword(rawPassword, player);
        return player;
    }

    private void checkOAuthOnly(Player player) {
        if (player.getPassword() == null || player.getPassword().isBlank()) {
            throw new OAuthOnlyLoginException(player.getOauthProvider());
        }
    }

    private void checkPassword(String rawPassword, Player player) {
        if (!passwordEncoder.matches(rawPassword, player.getPassword())) {
            throw new BadCredentialsException();
        }
    }
}