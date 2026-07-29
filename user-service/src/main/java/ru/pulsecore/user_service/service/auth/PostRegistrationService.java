package ru.pulsecore.user_service.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.Subscription;
import ru.pulsecore.user_service.repository.SubscriptionRepository;



@Component
@RequiredArgsConstructor
public class PostRegistrationService {

    private static final int TRIAL_DAYS = 7;


    private final SubscriptionRepository subscriptionRepository;


    @Transactional
    public void createTrial(Player player) {
        Subscription trial = Subscription.builder().player(player).build();
        trial.activate(TRIAL_DAYS);
        subscriptionRepository.save(trial);
    }


}