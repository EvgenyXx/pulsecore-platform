package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(String roleName) {
        super(HttpStatus.NOT_FOUND, "Роль не найдена: " + roleName);
    }
}