package ru.pulsecore.user_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;
import ru.pulsecore.shared.dto.player.PageViewStats;
import ru.pulsecore.shared.dto.player.PlayerPageViewStats;
import ru.pulsecore.user_service.service.internal.InternalPageViewService;

import java.util.List;

@Tag(name = "Статистика просмотров (internal)")
@RestController
@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
public class InternalPageViewController {

    private final InternalPageViewService internalPageViewService;

    @Operation(summary = "Статистика по страницам")
    @GetMapping(FeignPlayerApi.PAGE_VIEWS_STATS)
    public List<PageViewStats> getStats(@RequestParam int days) {
        return internalPageViewService.getStats(days);
    }

    @Operation(summary = "Статистика по игрокам")
    @GetMapping(FeignPlayerApi.PAGE_VIEWS_PLAYER_STATS)
    public List<PlayerPageViewStats> getPlayerStats(@RequestParam int days) {
        return internalPageViewService.getPlayerStats(days);
    }
}