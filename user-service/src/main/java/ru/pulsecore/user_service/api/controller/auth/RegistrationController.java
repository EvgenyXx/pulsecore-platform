package ru.pulsecore.user_service.api.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.user_service.api.PlayerApi;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;
import ru.pulsecore.user_service.api.dto.request.RegisterRequest;
import ru.pulsecore.user_service.api.dto.request.VerifyEmailRequest;
import ru.pulsecore.user_service.api.dto.response.AuthResponse;
import ru.pulsecore.user_service.service.CookieService;
import ru.pulsecore.user_service.service.auth.RegistrationFacade;

@Tag(name = "Авторизация")
@ApiV1PlayerController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationFacade registrationFacade;
    private final CookieService cookieService;

    @Operation(summary = "Зарегистрироваться")
    @PostMapping(PlayerApi.REGISTER)
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        registrationFacade.initiate(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new MessageResponse("ok"));
    }

    @Operation(summary = "Подтвердить email кодом")
    @PostMapping(PlayerApi.VERIFY_EMAIL)
    public ResponseEntity<AuthResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request,
                                                    HttpServletResponse response,
                                                    HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        var result = registrationFacade.complete(request.getEmail(), request.getCode(), ip, userAgent);
        cookieService.setRefreshTokenCookie(response, result.refreshToken());
        return ResponseEntity.ok(result.response());
    }
}