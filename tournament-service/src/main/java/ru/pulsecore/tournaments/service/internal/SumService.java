package ru.pulsecore.tournaments.service.internal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;

import ru.pulsecore.tournaments.persistence.repository.projection.PeriodStatsProjection;

import ru.pulsecore.tournaments.service.application.TournamentResultService;
import ru.pulsecore.tournaments.persistence.entity.TournamentResultEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SumService {

    private final TournamentResultService tournamentResultService;

    public SumResponse getSum(UUID playerId, LocalDate start, LocalDate end, int page, int size) {

        if (start == null) start = end;
        if (end == null) end = start;

        PeriodStatsProjection stats = tournamentResultService.getStatsByPeriod(playerId, start, end);
        Page<TournamentResultEntity> pageResult = tournamentResultService.getResultsByPeriod(
                playerId, start, end, PageRequest.of(page, size));

        return SumResponse.builder()
                .start(start.toString())
                .end(end.toString())
                .sum(stats != null ? stats.getSum() : 0)
                .average(stats != null ? stats.getAverage() : 0)
                .count(stats != null ? stats.getCount() : 0)
                .tournaments(buildTournamentItems(pageResult))
                .totalPages(pageResult.getTotalPages())
                .currentPage(pageResult.getNumber())
                .totalElements(pageResult.getTotalElements())
                .build();
    }



    private List<SumResponse.TournamentItem> buildTournamentItems(Page<TournamentResultEntity> pageResult) {
        return pageResult.getContent().stream()
                .map(e -> SumResponse.TournamentItem.builder()
                        .date(e.getDate().toString())
                        .amount(e.getAmount())
                        .resultId(e.getId())
                        .hasRemoved(e.isHasRemoved())
                        .build())
                .toList();
    }
}