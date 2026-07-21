package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class OAuthEmailAlreadyExistsException extends BaseException {
    public OAuthEmailAlreadyExistsException(String provider) {
        super(HttpStatus.CONFLICT,
                "Этот email уже используется через " + provider +
                        ". Войдите через " + provider + ".",
                "OAUTH_EMAIL");
    }
}