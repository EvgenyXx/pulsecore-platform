package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


import java.util.UUID;

public class PlayerNotFoundException extends BaseException {
    public PlayerNotFoundException(UUID playerId) {
        super(HttpStatus.NOT_FOUND, "Игрок не найден: " + playerId);
    }

    public PlayerNotFoundException(String name) {
        super(HttpStatus.NOT_FOUND, "Игрок не найден: " + name);
    }
}