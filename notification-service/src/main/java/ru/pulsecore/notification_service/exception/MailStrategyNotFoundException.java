package ru.pulsecore.notification_service.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;

public class MailStrategyNotFoundException extends BaseException {

    public MailStrategyNotFoundException(String type) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Почтовая стратегия не найдена: " + type);
    }
}