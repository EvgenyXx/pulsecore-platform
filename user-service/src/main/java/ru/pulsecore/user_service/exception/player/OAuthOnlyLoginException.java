package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class OAuthOnlyLoginException extends BaseException {
    public OAuthOnlyLoginException(String provider) {
        super(HttpStatus.FORBIDDEN,
                "Вы зарегистрированы через " + provider +
                        ". Войдите через " + provider + " или установите пароль в профиле.",
                "OAUTH_ONLY");
    }
}