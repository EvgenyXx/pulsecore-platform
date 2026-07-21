package ru.pulsecore.user_service.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.payment.PaymentSucceededEvent;
import ru.pulsecore.user_service.service.subscription.SubscriptionService;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionEventListener {

    private final SubscriptionService subscriptionService;

    @KafkaListener(topics = KafkaTopics.PLAYER_CREATED)
    public void handle(PaymentSucceededEvent event) {
        subscriptionService.activate(event.playerId(), event.days());
        log.info("Подписка активирована: playerId={}, days={}", event.playerId(), event.days());
    }
}