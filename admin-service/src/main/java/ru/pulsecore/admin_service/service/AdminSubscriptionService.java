package ru.pulsecore.admin_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.admin_service.client.PlayerClient;
import ru.pulsecore.shared.dto.player.SubscriptionStatusResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminSubscriptionService {

    private final PlayerClient  playerClient;

    public void activateSubscription(UUID playerId, int days) {
        playerClient.activateSubscription(playerId, days);
    }

    public void deactivateSubscription(UUID playerId) {
        playerClient.deactivateSubscription(playerId);
    }

    public SubscriptionStatusResponse getSubscriptionStatus(UUID playerId) {
     return  playerClient.getSubscription(playerId);
    }
}


