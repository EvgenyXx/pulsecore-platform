package ru.pulsecore.user_service.service.internal;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.dto.player.PageViewStats;
import ru.pulsecore.shared.dto.player.PlayerPageViewStats;
import ru.pulsecore.user_service.repository.PageViewRepository;
import ru.pulsecore.user_service.repository.PlayerStatsRepository;
import ru.pulsecore.user_service.service.page.labels.EndpointLabelRegistry;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalPageViewService {


    private final PageViewRepository pageViewRepository;
    private final EndpointLabelRegistry labelRegistry;
    private final PlayerStatsRepository playerStatsRepository;

    public List<PlayerPageViewStats> getPlayerStats(int days) {
        Instant from = Instant.now().minus(days, ChronoUnit.DAYS);
        return playerStatsRepository.getPlayerStats(from).stream()
                .map(row -> {
                    String[] pathPairs = row.getPaths().split("\\|");
                    long total = row.getTotal();

                    List<PlayerPageViewStats.PathCount> paths = Arrays.stream(pathPairs)
                            .map(pair -> {
                                int eq = pair.lastIndexOf('=');
                                String rawPath = pair.substring(0, eq);
                                long count = Long.parseLong(pair.substring(eq + 1));
                                return new PlayerPageViewStats.PathCount(labelRegistry.resolve(rawPath), count);
                            })
                            .toList();

                    return new PlayerPageViewStats(row.getName(), paths, total, row.getPercent());
                })
                .toList();
    }

    public List<PageViewStats> getStats(int days) {
        Instant from = Instant.now().minus(days, ChronoUnit.DAYS);

        return pageViewRepository.findStatsSince(from).stream()
                .map(p -> new PageViewStats(
                        labelRegistry.resolve(p.getPath()),
                        p.getMethod(),
                        p.getCount()
                ))
                .toList();
    }
}
