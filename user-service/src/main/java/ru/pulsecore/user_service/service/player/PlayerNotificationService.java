package ru.pulsecore.user_service.service.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.domain.Player;


import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerNotificationService {

    private final PlayerService playerService;


    public boolean isNotificationsEnabled(UUID id) {
        return playerService.getById(id).isNotificationsEnabled();
    }


    public void setNotificationsEnabled(UUID id, boolean enabled) {
        Player player = playerService.getById(id);
        player.setNotificationsEnabled(enabled);
        playerService.save(player);
        log.info("🔔 Уведомления {} для игрока {} ({})", enabled ? "включены" : "отключены", player.getName(), id);
    }

    public boolean togglePushEnabled(UUID playerId) {
        Player player = playerService.getById(playerId);
        player.setPushEnabled(!player.isPushEnabled());
        playerService.save(player);
        log.info("📲 Push-уведомления {} для игрока {} ({})", player.isPushEnabled() ? "включены" : "отключены", player.getName(), playerId);
        return player.isPushEnabled();
    }

    public boolean isPushEnabled(UUID playerId) {
        return playerService.getById(playerId).isPushEnabled();
    }
}