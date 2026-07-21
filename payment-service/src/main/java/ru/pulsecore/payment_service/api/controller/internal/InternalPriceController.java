package ru.pulsecore.payment_service.api.controller.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.payment_service.service.PriceService;
import ru.pulsecore.shared.config.constants.feighn.FeignPaymentApi;
import ru.pulsecore.shared.dto.payment.PricesResponse;
import ru.pulsecore.shared.dto.payment.UpdatePricesRequest;

@Tag(name = "Цены (internal)")
@RestController
@RequestMapping(FeignPaymentApi.BASE)
@RequiredArgsConstructor
public class InternalPriceController {

    private final PriceService priceService;

    @Operation(summary = "Получить цены (внутренний)")
    @GetMapping(FeignPaymentApi.PRICES)
    public ResponseEntity<PricesResponse> getPrices() {
        return ResponseEntity.ok(priceService.getPrices());
    }

    @Operation(summary = "Обновить цены на подписку")
    @PutMapping(FeignPaymentApi.UPDATE_PRICE)
    public ResponseEntity<Void> updatePrices(@RequestBody UpdatePricesRequest updatePricesRequest) {
        priceService.update(updatePricesRequest.oneMonth(), updatePricesRequest.twoMonths());
        return ResponseEntity.ok().build();
    }
}