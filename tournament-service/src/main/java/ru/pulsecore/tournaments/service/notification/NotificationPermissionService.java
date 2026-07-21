package ru.pulsecore.tournaments.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.client.PlayerClient;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationPermissionService {

    private final PlayerClient playerClient;

    public boolean canSendEmail(UUID playerId) {
        var settings = playerClient.getSettings(playerId);
        return settings.notificationsEnabled() && settings.hasActiveSubscription();
    }

    public boolean canSendPush(UUID playerId) {
        var settings = playerClient.getSettings(playerId);
        return settings.pushEnabled() && settings.hasActiveSubscription();
    }
}