package ru.pulsecore.payment_service.api.dto.response;

import ru.pulsecore.payment_service.api.dto.internal.ConfirmationUrl;

public record YookassaPaymentResponse(
        String id,
        ConfirmationUrl confirmation
) {}