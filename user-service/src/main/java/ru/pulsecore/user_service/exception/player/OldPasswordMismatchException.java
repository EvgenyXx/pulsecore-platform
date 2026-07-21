package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;

public class OldPasswordMismatchException extends BaseException {
    public OldPasswordMismatchException() {
        super(HttpStatus.BAD_REQUEST, "Старый пароль не совпадает");
    }
}