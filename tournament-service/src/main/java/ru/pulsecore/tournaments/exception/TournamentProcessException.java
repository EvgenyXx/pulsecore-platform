package ru.pulsecore.tournaments.exception;


import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;

public class TournamentProcessException extends BaseException {
    public TournamentProcessException(String link, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось обработать турнир: " + link);
        initCause(cause);
    }
}