package ru.pulsecore.tournaments.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;

public class SiteUnavailableException extends BaseException {
    public SiteUnavailableException() {

        super(HttpStatus.SERVICE_UNAVAILABLE, "Сайт недоступен, попробуйте позже");
    }
}