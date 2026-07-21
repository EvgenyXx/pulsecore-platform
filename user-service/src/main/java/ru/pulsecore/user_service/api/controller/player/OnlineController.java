package ru.pulsecore.user_service.api.controller.player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.dto.response.OnlineResponse;
import ru.pulsecore.user_service.service.online.OnlineService;

@Tag(name = "Online")
@RestController
@RequiredArgsConstructor
public class OnlineController {

    private final OnlineService onlineService;

    @Operation(summary = "Получить количество пользователей онлайн")
    @GetMapping(PlayerApi.ONLINE)
    public ResponseEntity<OnlineResponse> getOnline() {
        return ResponseEntity.ok(new OnlineResponse(onlineService.getOnlineCount()));
    }
}