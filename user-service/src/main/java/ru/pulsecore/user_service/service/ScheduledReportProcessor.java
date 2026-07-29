package ru.pulsecore.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.ScheduledReportContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.dto.tournament.request.SumRequest;
import ru.pulsecore.user_service.client.TournamentClient;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.ScheduledReport;

import ru.pulsecore.user_service.service.outbox.OutBoxService;
import ru.pulsecore.user_service.service.player.PlayerService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledReportProcessor {

    private final ScheduledReportService reportService;
    private final TournamentClient tournamentClient;
    private final PlayerService playerService;
    private final OutBoxService outBoxService;

    public void sendScheduledReports() {
        List<ScheduledReport> ready = reportService.findPendingBefore(LocalDateTime.now());
        for (ScheduledReport report : ready) {
            processReport(report);
        }
    }

    private void processReport(ScheduledReport report) {
        Player player = playerService.getById(report.getPlayer().getId());
        SumRequest request = new SumRequest(
                player.getId(), report.getDateFrom(), report.getDateTo(), 0, Integer.MAX_VALUE
        );

        var sum = tournamentClient.getSumForReport(request);

        if (sum.isFallback()) {
            return;
        }

        String period = report.getDateFrom() + " – " + report.getDateTo();
        outBoxService.save(KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(MailTypes.SCHEDULED_REPORT,
                        new ScheduledReportContext(
                                player.getEmail(), period,
                                String.format("%,.0f", sum.getSum()),
                                String.format("%,.0f", sum.getAverage()),
                                String.valueOf(sum.getCount() != null ? sum.getCount() : 0),
                                sum
                        )));

        reportService.markAsSent(report.getId());
        log.info("Отчёт отправлен: reportId={}, player={}", report.getId(), player.getEmail());
    }
}