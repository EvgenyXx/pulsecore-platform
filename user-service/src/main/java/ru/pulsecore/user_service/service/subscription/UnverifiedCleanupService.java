package ru.pulsecore.user_service.service.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.repository.PlayerRepository;
import ru.pulsecore.user_service.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnverifiedCleanupService {

    private final PlayerRepository playerRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public void cleanUnverified() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        List<Player> unverified = playerRepository.findByVerifiedFalseAndCreatedAtBefore(cutoff);

        for (Player p : unverified) {
            subscriptionRepository.deleteByPlayer(p);
        }
        playerRepository.deleteAll(unverified);

        log.info("Удалено неподтверждённых пользователей: {}", unverified.size());
    }
}