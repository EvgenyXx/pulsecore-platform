package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.api.dto.BroadcastRequest;
import ru.pulsecore.admin_service.service.BroadcastService;
import ru.pulsecore.shared.dto.MessageResponse;

@Tag(name = "Рассылка")
@AdminController
@RequiredArgsConstructor
public class BroadcastController {

    private final BroadcastService broadcastService;

    @Operation(summary = "Отправить массовую рассылку")
    @PostMapping(AdminApi.BROADCAST)
    public ResponseEntity<MessageResponse> broadcast(@RequestBody BroadcastRequest request) {
        return ResponseEntity.ok(
                new MessageResponse(broadcastService.broadcast(request.message()).toMessage())
        );
    }
}