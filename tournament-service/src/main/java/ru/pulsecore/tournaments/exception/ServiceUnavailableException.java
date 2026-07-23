package ru.pulsecore.tournaments.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;

public class ServiceUnavailableException extends BaseException {

    public ServiceUnavailableException(String serviceName) {
        super(HttpStatus.SERVICE_UNAVAILABLE,
                "Сервис \"" + serviceName + "\" временно недоступен. Попробуйте позже.");
    }



}