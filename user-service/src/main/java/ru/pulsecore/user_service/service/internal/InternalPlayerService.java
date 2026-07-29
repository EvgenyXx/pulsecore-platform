package ru.pulsecore.user_service.service.internal;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import ru.pulsecore.shared.dto.player.BroadcastRecipient;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.shared.dto.player.PlayerSettingsDto;
import ru.pulsecore.user_service.exception.player.PlayerNotFoundException;
import ru.pulsecore.user_service.repository.PlayerRepository;



import java.util.List;

import java.util.Set;
import java.util.UUID;



@RequiredArgsConstructor
@Service
public class InternalPlayerService {

    private final PlayerRepository playerRepository;


    public String getPlayerName(UUID playerId) {
        return playerRepository.findNameById(playerId);
    }


    public List<BroadcastRecipient> getBroadcastRecipients() {
        return playerRepository.findBroadcastRecipients().stream()
                .map(p -> new BroadcastRecipient(p.getId(), p.getEmail(), p.getPushEnabled()))
                .toList();
    }

    public List<PlayerSettingsDto> getSettings(Set<UUID> playerId) {
       return playerRepository.findSettingsByIds(playerId)
               .stream()
               .map(p ->
                       new PlayerSettingsDto(
                               p.getId(),
                               p.getPushEnabled(),
                               p.getNotificationsEnabled(),
                               p.getActiveSubscription())
                       ).toList();
    }

    public List<PlayerData> getPlayerDataByIds(Set<UUID> playerIds) {
        return playerRepository.findPlayerDataById(playerIds)
                .stream().map(
                        projection ->
                                new PlayerData(projection.getId(),projection.getName(),projection.getEmail())
                ).toList();
    }

    public List<PlayerSearchResponse>searchByName(String name) {
        return playerRepository.searchByName(name)
                .stream()
                .map(projection ->
                        new PlayerSearchResponse(
                                projection.getId(),
                                projection.getName(),
                                projection.getEmail()))
                .toList();
    }

    public UUID findIdByFullName(String fullName) {
        return playerRepository.findIdByNameIgnoreCase(fullName)
                .orElseThrow(()-> new PlayerNotFoundException(fullName));
    }
}
