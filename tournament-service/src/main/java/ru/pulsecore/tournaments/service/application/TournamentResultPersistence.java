package ru.pulsecore.tournaments.service.application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.persistence.repository.projection.PeriodStatsProjection;
import ru.pulsecore.tournaments.service.cache.CacheEvictionService;
import ru.pulsecore.tournaments.persistence.entity.TournamentResultEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentResultRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentResultPersistence {

    private final TournamentResultRepository tournamentResultRepository;
    private final CacheEvictionService cacheEvictionService;

    public Page<TournamentResultEntity> getResultsByPeriod(UUID playerId, LocalDate start, LocalDate end, Pageable pageable) {
        return tournamentResultRepository.findByPlayerIdAndDateBetweenOrderByDateAsc(playerId, start, end, pageable);
    }

    public List<TournamentResultEntity> save(List<TournamentResultEntity> entities) {
        List<TournamentResultEntity> toSave = new ArrayList<>();
        for (TournamentResultEntity entity : entities) {
            if (!existsByPlayerAndTournament(entity)) {
                toSave.add(entity);
            }
        }
        if (!toSave.isEmpty()) {
            List<TournamentResultEntity> saved = tournamentResultRepository.saveAll(toSave);
            evictCaches();
            return saved;
        }
        return entities;
    }


    public TournamentResultEntity saveOne(TournamentResultEntity entity) {
        if (!existsByPlayerAndTournament(entity)) {
            TournamentResultEntity saved = tournamentResultRepository.save(entity);
            evictCaches();
            return saved;
        }
        return entity;
    }

    public PeriodStatsProjection getStatsByPeriod(UUID playerId, LocalDate start, LocalDate end) {
        return tournamentResultRepository.getStats(playerId, start, end);
    }

    public void evictCaches() {
        cacheEvictionService.evictHallOfFame();
        cacheEvictionService.evictAnalytics();
    }

    private boolean existsByPlayerAndTournament(TournamentResultEntity entity) {
        return tournamentResultRepository.existsByPlayerIdAndTournament_ExternalId(
                entity.getPlayerId(), entity.getTournament().getExternalId());
    }
}