package ru.pulsecore.user_service.service.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.pulsecore.shared.config.CacheNames;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.SubscriptionExpiringContext;
import ru.pulsecore.shared.dto.player.SubscriptionStatusResponse;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.domain.Subscription;
import ru.pulsecore.user_service.repository.SubscriptionRepository;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisherIml;
import ru.pulsecore.user_service.service.player.PlayerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlayerService playerService;
    private final KafkaEventPublisherIml kafkaEventPublisherIml;



    @CacheEvict(value = CacheNames.SUBSCRIPTION, key = CacheNames.KEY_PLAYER_ID)
    @Transactional
    public void deactivate(UUID playerId) {
        Player player = playerService.getById(playerId);
        Subscription subscription = player.getSubscription();
        if (subscription != null) {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
            log.info("❌ Подписка отключена для {}", player.getEmail());
        }
    }

    @CacheEvict(value = CacheNames.SUBSCRIPTION, key = CacheNames.KEY_PLAYER_ID)
    @Transactional
    public void activate(UUID playerId, int days) {
        Player player = playerService.getById(playerId);
        Subscription subscription = player.getSubscription();
        if (subscription == null) {
            subscription = Subscription.builder().player(player).build();
        }
        subscription.activate(days);
        subscriptionRepository.save(subscription);
        log.info("✅ Подписка активирована для {} на {} дней", player.getEmail(), days);
    }

    public SubscriptionStatusResponse getByPlayerId(UUID playerId) {
        return subscriptionRepository.findSubscriptionByPlayerId(playerId)
                .map(s -> SubscriptionStatusResponse.builder()
                        .active(s.getActive())
                        .expiresAt(s.getExpiresAt() != null ? s.getExpiresAt().toString() : null)
                        .startedAt(s.getStartedAt() != null ? s.getStartedAt().toString() : null)
                        .build())
                .orElse(new SubscriptionStatusResponse(false, null, null));
    }

    @Cacheable(value = CacheNames.SUBSCRIPTION, key = CacheNames.KEY_PLAYER_ID)
    @Transactional(readOnly = true)
    public boolean hasActiveSubscription(UUID playerId) {
        var sub = subscriptionRepository.findByPlayerId(playerId);
        boolean active = sub.map(Subscription::isActiveNow).orElse(false);
        if (active) {
            Player player = sub.get().getPlayer();
            log.debug("✅ Активная подписка: {}", player.getName());
        }
        return active;
    }

    public int deactivateExpired() {
        List<Subscription> expired = subscriptionRepository.findExpired();
        expired.forEach(s -> s.setActive(false));
        subscriptionRepository.saveAll(expired);
        return expired.size();
    }


    public void notifyExpiring() {
        subscriptionRepository.findExpiringTomorrow().forEach(sub -> {
            Player player = sub.getPlayer();

            kafkaEventPublisherIml.publish(KafkaTopics.EMAIL_NOTIFICATION,
                    new MailNotificationEvent(MailTypes.SUBSCRIPTION_EXPIRING,
                            new SubscriptionExpiringContext(player.getEmail(), player.getName(),
                                    sub.getExpiresAt().toString())));

            kafkaEventPublisherIml.publish(KafkaTopics.PUSH_NOTIFICATION,
                    new PushNotificationEvent(player.getId(), "⏳ Подписка заканчивается!",
                            "Продлите подписку, чтобы не потерять доступ", "/subscribe"));
        });
    }
}