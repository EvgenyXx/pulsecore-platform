package ru.pulsecore.tournaments.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class MessageNotFoundException extends BaseException {
    public MessageNotFoundException(Long messageId) {
        super(HttpStatus.NOT_FOUND, "Сообщение не найдено :" + messageId );
    }
}