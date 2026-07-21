package ru.pulsecore.tournaments.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.service.notification.NotificationCleanupService;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationCleanupScheduler {

    private final NotificationCleanupService cleanupService;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {

        cleanupService.cleanup();
    }
}