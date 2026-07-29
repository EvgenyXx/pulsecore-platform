package ru.pulsecore.tournaments.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.dto.player.PlayerSettingsDto;
import ru.pulsecore.tournaments.client.PlayerClient;


import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationPermissionService {

    //todo добавить кеширование
    private final PlayerClient playerClient;

    public Map<UUID,Boolean> canSendEmail(Set<UUID> playerIds) {

        return playerClient.getSettings(playerIds)
                .stream()
                .collect(Collectors.toMap(
                        PlayerSettingsDto::playerId,
                        playerSettingsDto ->
                                playerSettingsDto.notificationsEnabled() && playerSettingsDto.hasActiveSubscription()
                ));
    }

    public  Map<UUID, Boolean> canSendPush(Set<UUID> playerIds) {
        return playerClient.getSettings(playerIds)
                .stream()
                .collect(Collectors.toMap(
                        PlayerSettingsDto::playerId,
                        playerSettingsDto ->
                                playerSettingsDto.pushEnabled() && playerSettingsDto.hasActiveSubscription()
                        )
                );
    }
}