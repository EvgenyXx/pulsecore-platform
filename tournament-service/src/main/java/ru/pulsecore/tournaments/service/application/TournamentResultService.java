package ru.pulsecore.tournaments.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.tournaments.persistence.repository.projection.PeriodStatsProjection;
import ru.pulsecore.tournaments.api.dto.ResultDto;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.entity.TournamentResultEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentResultService {

    private final TournamentResultPersistence persistence;
    private final TournamentResultProcessor processor;

    public Page<TournamentResultEntity> getResultsByPeriod(UUID playerId, LocalDate start, LocalDate end, Pageable pageable) {
        return persistence.getResultsByPeriod(playerId, start, end, pageable);
    }

    @Transactional
    public void updateResult(Long id, Double amount, Double bonus) {
        processor.updateResult(id, amount, bonus);
    }



    public PeriodStatsProjection getStatsByPeriod(UUID playerId, LocalDate start, LocalDate end) {
        return persistence.getStatsByPeriod(playerId, start, end);
    }

    public void processResultsForRoster(List<ResultDto> results,
                                        Map<UUID, String> roster,
                                        TournamentEntity tournament,
                                        double bonus,
                                        boolean isFinished,
                                        boolean hasRemoved,
                                        String league) {
        processor.processResults(results, roster, tournament, bonus, isFinished, hasRemoved, league);
    }

    public void processResultsForPlayer(List<ResultDto> results,
                                        UUID playerId, String playerName,
                                        TournamentEntity tournament,
                                        double bonus, boolean isFinished, boolean hasRemoved, String league) {
        processor.processResults(
                results,
                playerId,
                playerName,
                tournament,
                bonus,
                isFinished,
                hasRemoved,
                league);
    }
}