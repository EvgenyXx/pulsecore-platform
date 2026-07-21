package ru.pulsecore.user_service.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.Subscription;
import ru.pulsecore.user_service.repository.SubscriptionRepository;

@Component
@RequiredArgsConstructor
public class TrialActivator {

    private static final int TRIAL_DAYS = 7;

    private final SubscriptionRepository subscriptionRepository;

    public void activate(Player player) {
        Subscription trial = Subscription.builder().player(player).build();
        trial.activate(TRIAL_DAYS);
        subscriptionRepository.save(trial);
    }
}