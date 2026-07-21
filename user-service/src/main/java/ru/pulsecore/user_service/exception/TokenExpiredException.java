package ru.pulsecore.user_service.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "Refresh токен истёк");
    }
}