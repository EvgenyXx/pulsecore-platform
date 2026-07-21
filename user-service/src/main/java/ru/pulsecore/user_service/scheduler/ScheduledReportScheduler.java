package ru.pulsecore.user_service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.ScheduledReportContext;
import ru.pulsecore.shared.dto.tournament.request.SumRequest;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.util.FeignUtils;
import ru.pulsecore.user_service.client.TournamentClient;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.ScheduledReport;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;
import ru.pulsecore.user_service.service.player.PlayerService;


import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledReportScheduler {

    private final ScheduledReportService reportService;
    private final TournamentClient tournamentClient;
    private final PlayerService playerService;
    private final KafkaEventPublisher playerEventPublisher;

    @Scheduled(fixedDelay = 60000)
    public void sendScheduledReports() {
        List<ScheduledReport> ready = reportService.findPendingBefore(LocalDateTime.now());

        for (ScheduledReport report : ready) {
            Player player = playerService.getById(report.getPlayer().getId());

            SumRequest request = new SumRequest(
                    player.getId(),
                    report.getDateFrom(),
                    report.getDateTo(),
                    0,
                    Integer.MAX_VALUE
            );


            FeignUtils.tryExecute(
                    () -> tournamentClient.getSumForReport(request),
                    "tournament-service"
            ).ifPresent(sum -> {
                String period = report.getDateFrom() + " – " + report.getDateTo();

                playerEventPublisher.publish(
                        KafkaTopics.EMAIL_NOTIFICATION,
                        new MailNotificationEvent(
                                MailTypes.SCHEDULED_REPORT,
                                new ScheduledReportContext(
                                        player.getEmail(),
                                        period,
                                        String.format("%,.0f", sum.getSum()),
                                        String.format("%,.0f", sum.getAverage()),
                                        String.valueOf(sum.getCount() != null ? sum.getCount() : 0),
                                        sum
                                )
                        ));

                reportService.markAsSent(report.getId());
                log.info("Отчёт {} отправлен игроку {}", report.getId(), player.getEmail());
            });


        }
    }

}