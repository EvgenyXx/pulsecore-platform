package ru.pulsecore.admin_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.admin_service.client.PlayerClient;
import ru.pulsecore.shared.dto.player.RoleRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminRoleService {

    private final PlayerClient playerClient;

    public void grantRole(UUID playerId, RoleRequest request) {
        playerClient.grantRole(playerId, request);
    }

    public void revokeRole(UUID playerId, RoleRequest request) {
        playerClient.revokeRole(playerId, request);
    }

    public List<String> getRoles(UUID playerId) {
        return playerClient.getRoles(playerId);
    }
}