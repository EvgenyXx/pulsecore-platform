package ru.pulsecore.user_service.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.service.subscription.UnverifiedCleanupService;

@Service
@RequiredArgsConstructor
public class UnverifiedCleanupScheduled {

    private final UnverifiedCleanupService unverifiedCleanupService;


    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUnverified() {
        unverifiedCleanupService.cleanUnverified();
    }
}
