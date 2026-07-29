package ru.pulsecore.tournaments.service.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.service.application.TournamentResultService;
import ru.pulsecore.tournaments.domain.ParsedResult;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;
import ru.pulsecore.tournaments.service.cache.PlayerCache;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentProcessService {

    private final TournamentResultService tournamentResultService;
    private final TournamentRepository tournamentRepository;
    private final PlayerClient playerClient;
    private final PlayerCache playerCache;

    @Transactional
    public void processTournament(
            List<PlayerNotification> notifications,
            ParsedResult parsed) {
        if (notifications == null || notifications.isEmpty()) return;
        TournamentEntity tournament = notifications.get(0).getTournament();
        if (tournament == null) return;

        updateTournamentDates(tournament, parsed);

        Set<UUID> playerIds = notifications.stream()
                        .map(PlayerNotification::getPlayerId)
                                .collect(Collectors.toSet());

        List<PlayerData>roster = playerClient.getPlayerDataByIds(playerIds);

        playerCache.updateRosterPlayers(tournament.getLink(),roster);

        Map<UUID,String> rosterData = roster.stream()
                        .collect(Collectors.toMap(
                                PlayerData::id,
                                PlayerData::name
                        ));
        processPlayerResults(rosterData, parsed, tournament);

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
            Map<UUID, String> roster,
            ParsedResult parsed,
            TournamentEntity tournament) {
        tournamentResultService.processResultsForRoster(
                parsed.results(), roster, tournament,
                parsed.nightBonus(),
                parsed.isFinished() || parsed.isFinalRemoved(),
                parsed.hasRemoved(),
                parsed.league());
    }
}