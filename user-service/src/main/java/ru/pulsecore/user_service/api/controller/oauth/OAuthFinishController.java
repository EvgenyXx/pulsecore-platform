package ru.pulsecore.user_service.api.controller.oauth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.api.dto.request.OAuthFinishRequest;
import ru.pulsecore.user_service.service.oauth.OAuthFinishService;

@Tag(name = "Авторизация")
@ApiV1PlayerController
@RequiredArgsConstructor
public class OAuthFinishController {

    private final OAuthFinishService service;

    @Operation(summary = "Завершить OAuth-авторизацию через Яндекс")
    @PostMapping(PlayerApi.OAUTH_FINISH)
    public ResponseEntity<String> finishOAuth(@RequestBody OAuthFinishRequest request,
                                              HttpServletRequest httpRequest) {
        service.complete(request, httpRequest);
        return ResponseEntity.ok("ok");
    }
}