package ru.pulsecore.payment_service.api.dto.reqauest;

import ru.pulsecore.payment_service.api.dto.internal.Amount;
import ru.pulsecore.payment_service.api.dto.internal.Confirmation;

import java.util.Map;

public record YookassaPaymentRequest(
        Amount amount,
        Confirmation confirmation,
        String description,
        Map<String, String> metadata,
        boolean capture
) {}
