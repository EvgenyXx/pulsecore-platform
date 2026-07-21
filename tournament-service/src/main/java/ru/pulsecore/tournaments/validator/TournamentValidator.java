package ru.pulsecore.tournaments.validator;

import org.springframework.stereotype.Component;
import ru.pulsecore.shared.dto.tournament.TournamentDto;

@Component
public class TournamentValidator {

    public boolean isValid(TournamentDto t) {
        return t.getPlayers() != null && !t.getPlayers().isEmpty();
    }
}