package ru.pulsecore.tournaments.service.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.exception.TournamentParseException;

import ru.pulsecore.tournaments.service.application.ResultService;
import ru.pulsecore.tournaments.service.application.TournamentResultService;
import ru.pulsecore.tournaments.domain.ParsedResult;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;

import java.time.LocalDate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentUrlProcessor {


    private final TournamentResultService tournamentResultService;
    private final ResultService resultService;
    private final TournamentRepository tournamentRepository;



    public void processByUrl(String url, UUID playerId, String playerName) {

        ParsedResult parsed = parseUrl(url);
        TournamentEntity tournament = findOrCreateTournament(parsed, url);
        updateTournamentDates(tournament, parsed);

        tournamentResultService.processResults(
                parsed.results(), playerId, playerName, parsed.tournamentId(),
                parsed.nightBonus(),
                parsed.isFinished() || parsed.isFinalRemoved(),
                parsed.hasRemoved(),
                parsed.league());


    }


    private ParsedResult parseUrl(String url) {
        try {
            return resultService.calculateAll(url);
        } catch (Exception e) {
            throw new TournamentParseException(url, e);
        }
    }

    private TournamentEntity findOrCreateTournament(ParsedResult parsed, String url) {
        return tournamentRepository.findByExternalId(parsed.tournamentId())
                .orElseGet(() -> tournamentRepository.save(TournamentEntity.builder()
                        .externalId(parsed.tournamentId())
                        .link(url)
                        .build()));
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



}