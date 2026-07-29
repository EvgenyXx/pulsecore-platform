package ru.pulsecore.tournaments.service.application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.tournaments.api.dto.ResultDto;
import ru.pulsecore.tournaments.service.parser.PlayerNameMatcher;
import ru.pulsecore.tournaments.exception.TournamentResultNotFoundException;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.entity.TournamentResultEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentResultRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentResultProcessor {

    private final TournamentResultRepository tournamentResultRepository;

    private final TournamentResultPersistence persistence;

    @Transactional
    public void updateResult(Long id, Double amount, Double bonus) {
        TournamentResultEntity result = tournamentResultRepository.findById(id)
                .orElseThrow(() -> new TournamentResultNotFoundException(id));
        if (amount != null) result.setAmount(amount);
        if (bonus != null) result.setBonus(bonus);
        tournamentResultRepository.save(result);
        persistence.evictCaches();
    }

    public void processResults(List<ResultDto> results,
                               Map<UUID, String> roster,
                               TournamentEntity tournament,
                               double bonus,
                               boolean isFinished,
                               boolean hasRemoved,
                               String league) {
        if (!isFinished) return;
        List<TournamentResultEntity> entities = new ArrayList<>();
        for (ResultDto r : results) {
            roster.forEach((playerId, playerName) -> {
                if (PlayerNameMatcher.isSamePlayer(playerName, r.getPlayer())) {
                    entities.add(buildEntity(playerId, tournament, r, bonus, hasRemoved, league));
                }
            });
        }
        if (!entities.isEmpty()) {
            persistence.save(entities);
        }
    }

    public void processResults(List<ResultDto> results,
                               UUID playerId,
                               String playerName,
                               TournamentEntity tournament,
                               double bonus,
                               boolean isFinished,
                               boolean hasRemoved,
                               String league) {
        if (!isFinished) return;
        for (ResultDto r : results) {
            if (PlayerNameMatcher.isSamePlayer(playerName, r.getPlayer())) {
                persistence.saveOne(buildEntity(playerId, tournament, r, bonus, hasRemoved, league));
            }
        }
    }

    private TournamentResultEntity buildEntity(UUID playerId, TournamentEntity tournament, ResultDto r,
                                               double bonus, boolean hasRemoved, String league) {
        return TournamentResultEntity.builder()
                .playerId(playerId)
                .amount((double) r.getTotal())
                .date(LocalDate.parse(r.getDate()))
                .tournament(tournament)
                .isNight(bonus > 0)
                .bonus(bonus)
                .hasRemoved(hasRemoved)
                .league(league)
                .build();
    }
}