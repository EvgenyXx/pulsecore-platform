package ru.pulsecore.user_service.exception.player;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class SubscriptionRequiredException extends BaseException {
    public SubscriptionRequiredException() {
        super(HttpStatus.PAYMENT_REQUIRED, "Требуется активная подписка");
    }
}