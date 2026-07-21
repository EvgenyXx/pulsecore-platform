package ru.pulsecore.tournaments.exception;


import org.springframework.http.HttpStatus;
import ru.pulsecore.shared.exception.BaseException;


public class TournamentNotFoundException extends BaseException {
    public TournamentNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Турнир не найден: " + id);
    }
}