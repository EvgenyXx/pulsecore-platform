package ru.pulsecore.user_service.scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.api.dto.response.ScheduledReportResponse;
import ru.pulsecore.user_service.service.player.PlayerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduledReportFacade {

    private final ScheduledReportService reportService;
    private final PlayerService playerService;

    public void createReport(UUID playerId, LocalDate from, LocalDate to, LocalDateTime scheduledAt) {
        var player = playerService.getById(playerId);
        reportService.create(player, from, to, scheduledAt);
    }

    public List<ScheduledReportResponse> getPlayerReports(UUID playerId) {
        return reportService.findPendingByPlayer(playerId).stream()
                .map(ScheduledReportResponse::from)
                .toList();
    }


    public void deleteByScheduled(UUID scheduled) {
        reportService.deleteByScheduled(scheduled);
    }
}