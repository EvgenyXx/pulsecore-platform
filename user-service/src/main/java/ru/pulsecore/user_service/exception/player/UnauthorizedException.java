package ru.pulsecore.user_service.exception.player;


import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "Требуется авторизация");
    }
}