package ru.pulsecore.tournaments.service.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.service.application.TournamentResultService;
import ru.pulsecore.tournaments.domain.ParsedResult;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentProcessService {

    private final TournamentResultService tournamentResultService;
    private final TournamentRepository tournamentRepository;

    @Transactional
    public void processTournament(List<PlayerNotification> notifications, ParsedResult parsed,String playerName) {
        if (notifications == null || notifications.isEmpty()) return;
        TournamentEntity tournament = notifications.get(0).getTournament();
        if (tournament == null) return;

        updateTournamentDates(tournament, parsed);

        for (PlayerNotification pn : notifications) {
            UUID playerId = pn.getPlayerId();
            if (playerId == null) continue;
            processPlayerResults(playerId,playerName, parsed);
        }

        tournament.setFinished(true);
    }

    private void updateTournamentDates(TournamentEntity tournament, ParsedResult parsed) {
        if (tournament.getDate() == null) {
            tournament.setDate(extractDate(parsed));
        }
        if (tournament.getTime() == null && parsed.time() != null && !parsed.time().isEmpty()) {
            tournament.setTime(parsed.time());
        }
        tournamentRepository.save(tournament);
    }

    private LocalDate extractDate(ParsedResult parsed) {
        if (parsed.results().isEmpty()) return null;
        String dateStr = parsed.results().get(0).getDate();
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private void processPlayerResults(
            UUID playerId,
            String playerName,
            ParsedResult parsed) {
        tournamentResultService.processResults(
                parsed.results(), playerId, playerName,parsed.tournamentId(),
                parsed.nightBonus(),
                parsed.isFinished() || parsed.isFinalRemoved(),
                parsed.hasRemoved(),
                parsed.league());
    }
}