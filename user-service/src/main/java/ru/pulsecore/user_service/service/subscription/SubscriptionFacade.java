package ru.pulsecore.user_service.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.player.SubscriptionStatusResponse;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionFacade {

    private final SubscriptionService subscriptionService;

    public SubscriptionStatusResponse getSubscription(UUID playerId) {
        SubscriptionStatusResponse sub = subscriptionService.getByPlayerId(playerId);
        if (sub == null) {
            return new SubscriptionStatusResponse(false, null, null);
        }
        return sub;
    }
}