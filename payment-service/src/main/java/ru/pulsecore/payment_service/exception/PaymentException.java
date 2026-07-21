package ru.pulsecore.payment_service.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class PaymentException extends BaseException {
    public PaymentException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }
}