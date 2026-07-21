package ru.pulsecore.tournaments.exception;

import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class TournamentResultNotFoundException extends BaseException {
    public TournamentResultNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Результат турнира не найден: " + id);
    }
}