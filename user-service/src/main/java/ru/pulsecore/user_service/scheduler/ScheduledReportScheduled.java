package ru.pulsecore.user_service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pulsecore.user_service.service.ScheduledReportProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledReportScheduled {


    private final ScheduledReportProcessor scheduledReportProcessor;

    @Scheduled(fixedDelay = 60000)
    public void sendScheduledReports() {
        scheduledReportProcessor.sendScheduledReports();
    }

}