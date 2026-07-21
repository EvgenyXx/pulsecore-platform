package ru.pulsecore.user_service.mapping;

import org.springframework.stereotype.Component;
import ru.pulsecore.user_service.api.dto.response.AuthResponse;
import ru.pulsecore.user_service.domain.Player;

@Component
public class PlayerDtoMapper {
    public AuthResponse toAuthResponse(Player player) {
        return AuthResponse.builder()
                .id(player.getId().toString())
                .name(player.getName())
                .email(player.getEmail())
                .build();
    }
}