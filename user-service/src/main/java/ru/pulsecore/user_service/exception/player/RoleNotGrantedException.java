package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class RoleNotGrantedException extends BaseException {
    public RoleNotGrantedException(String roleName) {
        super(HttpStatus.NOT_FOUND, "У игрока нет роли: " + roleName);
    }
}