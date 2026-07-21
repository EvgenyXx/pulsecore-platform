package ru.pulsecore.admin_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.admin_service.client.PlayerClient;
import ru.pulsecore.shared.dto.player.PageViewStats;
import ru.pulsecore.shared.dto.player.PlayerPageViewStats;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PageViewStatsService {

    private final PlayerClient playerClient;

    public List<PageViewStats> getStats(int days) {
        return playerClient.getPageViewStats(days);
    }

    public List<PlayerPageViewStats> getPlayerStats(int days) {
        return playerClient.getPlayerPageViewStats(days);
    }
}