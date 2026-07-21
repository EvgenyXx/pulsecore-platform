package ru.pulsecore.tournaments.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class TournamentParseException extends BaseException {
    public TournamentParseException(String url, Exception cause) {
        super(HttpStatus.BAD_REQUEST, "Ошибка парсинга турнира: " + cause.getMessage());
    }
}