package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class SamePasswordException extends BaseException {
    public SamePasswordException() {
        super(HttpStatus.BAD_REQUEST, "Новый пароль не должен совпадать со старым");
    }
}