package ru.pulsecore.tournaments.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.shared.dto.player.PlayerSettingsDto;
import ru.pulsecore.tournaments.exception.ServiceUnavailableException;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import ru.pulsecore.tournaments.service.cache.PlayerCache;

import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class PlayerClientFallbackFactory implements FallbackFactory<PlayerClient> {

    private final PlayerCache  playerCache;
    private final PlayerNotificationRepository notificationRepository;

    @Override
    public PlayerClient create(Throwable cause) {
        return new PlayerClient() {
            @Override
            public List<PlayerSearchResponse> searchByName(String q) {
                return List.of();
            }//todo временно не доступен

            @Override
            public UUID getIdByFullName(String name) {
                return null;
            }//todo временно не доступен

            @Override
            public List<PlayerSettingsDto> getSettings(@RequestParam Set<UUID> playerId){
                return List.of();
            }

            @Override
            public String getLiveSelectedHalls(UUID playerId) {
                log.info("ФОЛЛ БЕК ОТРАБОТАЛ");
                throw new ServiceUnavailableException("USER-SERVICE1" + cause.getMessage());
            }//todo нужно кешировать

            @Override
            public List<PlayerData> getAllActivePlayers() {
                return playerCache.getActivePlayers();
            }

            @Override
            public String getSelectedHalls(UUID playerId) {
                log.info("ФОЛЛ БЕК ОТРАБОТАЛ");
                throw new ServiceUnavailableException("USER-SERVICE3");
            }//todo нужно кешировать

            @Override
            public String getPlayerName(UUID playerId) {
                return "";//todo нужно кешировать
            }

            @Override
            public List<PlayerData> getPlayerDataByIds(Set<UUID> playerId) {
//                List<PlayerNotification>notifications= notificationRepository
//
                return List.of();
            }
        };
    }
}