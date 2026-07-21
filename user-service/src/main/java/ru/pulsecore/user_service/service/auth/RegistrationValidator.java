package ru.pulsecore.user_service.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.user_service.exception.player.EmailAlreadyExistsException;
import ru.pulsecore.user_service.exception.player.OAuthOnlyLoginException;
import ru.pulsecore.user_service.exception.player.PlayerNameAlreadyExistsException;
import ru.pulsecore.user_service.repository.PlayerRepository;

@Component
@RequiredArgsConstructor
public class RegistrationValidator {

    private final PlayerRepository playerRepository;

    public void validate(String email, String name) {
        String normalizedEmail = email.toLowerCase().trim();
        String normalizedName = name.toLowerCase().trim();

        playerRepository.findByEmail(normalizedEmail).ifPresent(player -> {
            if (player.getPassword() == null || player.getPassword().isBlank()) {
                throw new OAuthOnlyLoginException(player.getOauthProvider());
            }
            throw new EmailAlreadyExistsException();
        });

        if (playerRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new PlayerNameAlreadyExistsException();
        }
    }
}