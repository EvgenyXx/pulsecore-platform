package ru.pulsecore.tournaments.service.application;

import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.domain.ParsedResult;

import java.util.Map;
import java.util.UUID;

@Service
public class ParticipationService {

    public boolean isAnyUserInParsed(ParsedResult parsed, Map<UUID, String> roster) {
        if (parsed == null || parsed.results() == null || roster.isEmpty()) return false;

        return parsed.results().stream()
                .anyMatch(r -> r.getPlayer() != null && roster.containsValue(r.getPlayer()));
    }
}//todo подозрения