package ru.pulsecore.user_service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pulsecore.user_service.service.subscription.SubscriptionService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduled {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 * * * *")
    public void deactivateExpired() {
       subscriptionService.deactivateExpired();
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void checkExpiringSubscriptions() {
        subscriptionService.notifyExpiring();
    }
}