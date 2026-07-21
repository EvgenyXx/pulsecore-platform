package ru.pulsecore.admin_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.admin_service.client.PlayerClient;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminPlayerService {

    private final PlayerClient  playerClient;


    public MessageResponse deleteAccount(UUID playerId) {
        playerClient.deletePlayer(playerId);
        return new MessageResponse("Игрок успешно удален");
    }

    public List<PlayerSearchResponse> search(String query) {
        return playerClient.searchPlayers(query);
    }
}
