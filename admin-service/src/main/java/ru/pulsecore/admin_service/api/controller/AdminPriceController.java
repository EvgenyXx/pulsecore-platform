package ru.pulsecore.admin_service.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pulsecore.admin_service.api.AdminApi;
import ru.pulsecore.admin_service.api.annatation.AdminController;
import ru.pulsecore.admin_service.api.dto.UpdatePricesRequest;
import ru.pulsecore.admin_service.service.AdminPaymentService;
import ru.pulsecore.shared.dto.payment.PricesResponse;

@Tag(name = "Управление ценами")
@RequiredArgsConstructor
@AdminController
public class AdminPriceController {

    private final AdminPaymentService paymentService;

    @Operation(summary = "Обновить цены на подписку")
    @PutMapping(AdminApi.PRICES)
    public ResponseEntity<PricesResponse> updatePrices(@Valid @RequestBody UpdatePricesRequest request) {
        paymentService.updatePrice(request);
        return ResponseEntity.ok(paymentService.getPrice());
    }
}