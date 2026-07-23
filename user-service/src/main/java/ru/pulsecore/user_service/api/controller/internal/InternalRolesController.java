package ru.pulsecore.user_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;

import ru.pulsecore.shared.dto.player.RoleRequest;
import ru.pulsecore.user_service.service.role.RoleManagementService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Роли (internal)")
@RestController
@RequestMapping(FeignPlayerApi.BASE)
@RequiredArgsConstructor
public class InternalRolesController {

    private final RoleManagementService roleManagementService;

    @Operation(summary = "Выдать роль игроку")
    @PostMapping(FeignPlayerApi.GRANT_ROLE)
    public ResponseEntity<Void> grantRole(@PathVariable UUID playerId, @RequestBody RoleRequest request) {
        roleManagementService.grantRole(playerId, request.roleName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отозвать роль у игрока")
    @PostMapping(FeignPlayerApi.REVOKE_ROLE)
    public ResponseEntity<Void> revokeRole(@PathVariable UUID playerId, @RequestBody RoleRequest request) {
        roleManagementService.revokeRole(playerId, request.roleName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить список ролей игрока")
    @GetMapping(FeignPlayerApi.GET_ROLES)
    public ResponseEntity<List<String>> getRoles(@PathVariable UUID playerId) {
        return ResponseEntity.ok(roleManagementService.getRoleNames(playerId));
    }
}