package ru.pulsecore.user_service.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.Subscription;
import ru.pulsecore.user_service.repository.SubscriptionRepository;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;


@Component
@RequiredArgsConstructor
public class PostRegistrationService {

    private static final int TRIAL_DAYS = 7;
    private static final int RECENT_DAYS = 30;

    private final SubscriptionRepository subscriptionRepository;

    private final KafkaEventPublisher publisher;

    @Transactional
    public void execute(Player player) {
        createTrial(player);
        publisher.publish(
                KafkaTopics.PLAYER_CREATED,
                new PlayerCreatedEvent(player.getId(), player.getName(), player.getEmail(), RECENT_DAYS));

    }

    private void createTrial(Player player) {
        Subscription trial = Subscription.builder().player(player).build();
        trial.activate(TRIAL_DAYS);
        subscriptionRepository.save(trial);
    }


}