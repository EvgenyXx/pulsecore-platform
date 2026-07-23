package ru.pulsecore.tournaments.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.shared.dto.player.PlayerSettingsDto;
import ru.pulsecore.tournaments.exception.ServiceUnavailableException;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class PlayerClientFallbackFactory implements FallbackFactory<PlayerClient> {
    @Override
    public PlayerClient create(Throwable cause) {
        return new PlayerClient() {
            @Override
            public List<PlayerSearchResponse> searchByName(String q) {
                return List.of();
            }

            @Override
            public UUID getIdByFullName(String name) {
                return null;
            }

            @Override
            public PlayerSettingsDto getSettings(UUID playerId) {
                return null;
            }

            @Override
            public String getLiveSelectedHalls(UUID playerId) {
                log.info("ФОЛЛ БЕК ОТРАБОТАЛ");
                throw new ServiceUnavailableException("USER-SERVICE");
            }

            @Override
            public List<PlayerData> getAllActivePlayers() {
                log.error("user-service недоступен: {}", cause.getMessage());
                throw new ServiceUnavailableException("Сервис временно недоступен. Попробуйте позже.");
            }

            @Override
            public String getSelectedHalls(UUID playerId) {
                log.info("ФОЛЛ БЕК ОТРАБОТАЛ");
                throw new ServiceUnavailableException("Сервис временно недоступен. Попробуйте позже.");
            }

            @Override
            public String getPlayerName(UUID playerId) {
                return "";
            }

            @Override
            public PlayerData getById(UUID playerId) {
                return null;
            }
        };
    }
}