package ru.pulsecore.payment_service.api.controller.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import ru.pulsecore.payment_service.api.YookassaApi;
import ru.pulsecore.payment_service.api.annatation.ApiV1PaymentController;
import ru.pulsecore.payment_service.service.PriceService;
import ru.pulsecore.shared.dto.payment.PricesResponse;

@Tag(name = "Цены")
@ApiV1PaymentController
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @Operation(summary = "Получить цены на подписку")
    @GetMapping(YookassaApi.PRICES)
    public ResponseEntity<PricesResponse> getPrices() {
        return ResponseEntity.ok(priceService.getPrices());
    }
}