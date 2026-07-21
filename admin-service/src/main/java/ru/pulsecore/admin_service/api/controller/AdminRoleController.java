package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.service.AdminRoleService;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.shared.dto.player.RoleRequest;


import java.util.List;
import java.util.UUID;

@Tag(name = "Управление ролями")

@AdminController
@RequiredArgsConstructor
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    @Operation(summary = "Выдать роль игроку")
    @PostMapping(AdminApi.ROLES_GRANT)
    public ResponseEntity<MessageResponse> grantRole(@PathVariable UUID playerId, @RequestBody RoleRequest request) {
        adminRoleService.grantRole(playerId, request);
        return ResponseEntity.ok(new MessageResponse("Роль " + request.roleName() + " выдана"));
    }

    @Operation(summary = "Отозвать роль у игрока")
    @DeleteMapping(AdminApi.ROLES_REVOKE)
    public ResponseEntity<MessageResponse> revokeRole(@PathVariable UUID playerId, @RequestBody RoleRequest request) {
        adminRoleService.revokeRole(playerId, request);
        return ResponseEntity.ok(new MessageResponse("Роль " + request.roleName() + " отозвана"));
    }

    @Operation(summary = "Получить роли игрока")
    @GetMapping(AdminApi.ROLES)
    public ResponseEntity<List<String>> getRoles(@PathVariable UUID playerId) {
        return ResponseEntity.ok(adminRoleService.getRoles(playerId));
    }
}