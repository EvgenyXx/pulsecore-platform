package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class BadResetCodeException extends BaseException {
    public BadResetCodeException() {
        super(HttpStatus.BAD_REQUEST, "Неверный код сброса");
    }
}