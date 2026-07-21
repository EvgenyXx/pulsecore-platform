package ru.pulsecore.admin_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.admin_service.client.TournamentClient;
import ru.pulsecore.admin_service.client.PlayerClient;
import ru.pulsecore.shared.dto.tournament.response.AdminCalculateResponse;
import ru.pulsecore.shared.dto.tournament.request.CalculateRequest;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminTournamentService {

    private final TournamentClient adminTournamentClient;
    private final PlayerClient playerClient;

    public int deletePlayerTournaments(UUID playerId) {
        return adminTournamentClient.deletePlayerTournaments(playerId);
    }

    public void resyncPlayerTournaments(UUID playerId) {
        var player = playerClient.getById(playerId);
        adminTournamentClient.resyncPlayerTournaments(player.id(),player.name());
    }

    public AdminCalculateResponse calculate( CalculateRequest request) {
        return adminTournamentClient.calculate(request);
    }
}