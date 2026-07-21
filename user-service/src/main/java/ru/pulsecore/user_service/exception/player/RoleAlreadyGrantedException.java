package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class RoleAlreadyGrantedException extends BaseException {
    public RoleAlreadyGrantedException(String roleName) {
        super(HttpStatus.CONFLICT, "Игрок уже имеет роль: " + roleName);
    }
}