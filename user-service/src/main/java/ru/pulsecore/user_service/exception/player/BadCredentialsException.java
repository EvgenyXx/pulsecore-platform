package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class BadCredentialsException extends BaseException {
    public BadCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Неверный email или пароль");
    }
}