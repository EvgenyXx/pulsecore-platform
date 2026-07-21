package ru.pulsecore.payment_service.api.controller.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pulsecore.payment_service.api.YookassaApi;
import ru.pulsecore.payment_service.api.annatation.ApiV1PaymentController;
import ru.pulsecore.payment_service.api.dto.response.PaymentResponse;
import ru.pulsecore.payment_service.service.YookassaService;

import java.util.UUID;

@Tag(name = "Платежи")
@ApiV1PaymentController
@RequiredArgsConstructor
public class PaymentController {

    private final YookassaService yookassaService;

    @Operation(summary = "Создать платёж")
    @PostMapping(YookassaApi.PAY)
    public ResponseEntity<PaymentResponse> pay(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam int months) {
        return ResponseEntity.ok(yookassaService.createPayment(UUID.fromString(jwt.getSubject()), months));
    }
}