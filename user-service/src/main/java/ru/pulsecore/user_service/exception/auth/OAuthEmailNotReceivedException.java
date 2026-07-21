package ru.pulsecore.user_service.exception.auth;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class OAuthEmailNotReceivedException extends BaseException {
    public OAuthEmailNotReceivedException() {
        super(HttpStatus.BAD_REQUEST, "Не удалось получить email от OAuth-провайдера. Попробуйте позже.");
    }
}