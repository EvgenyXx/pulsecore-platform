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

    public PlayerSettingsDto getSettings(UUID playerId) {
        var player = playerRepository.findSettingsById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
        return new PlayerSettingsDto(
                player.getPushEnabled(),
                player.getNotificationsEnabled(),
                player.getActiveSubscription()
        );
    }

    public PlayerData getById(UUID playerId) {
        var player = playerRepository.findDataById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
        return new PlayerData(player.getId(), player.getName(), player.getEmail());
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
