package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.service.PageViewStatsService;
import ru.pulsecore.shared.dto.player.PageViewStats;
import ru.pulsecore.shared.dto.player.PlayerPageViewStats;

import java.util.List;

@Tag(name = "Статистика просмотров")
@AdminController
@RequiredArgsConstructor
public class PageViewController {

    private final PageViewStatsService pageViewStatsService;

    @Operation(summary = "Статистика по страницам")
    @GetMapping(AdminApi.PAGE_VIEWS_STATS)
    public ResponseEntity<List<PageViewStats>> getStats(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(pageViewStatsService.getStats(days));
    }

    @Operation(summary = "Статистика по игрокам")
    @GetMapping(AdminApi.PAGE_VIEWS_PLAYERS)
    public ResponseEntity<List<PlayerPageViewStats>> getPlayerStats(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(pageViewStatsService.getPlayerStats(days));
    }
}