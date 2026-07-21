package ru.pulsecore.payment_service.api.dto.response;

public record PaymentResponse(String confirmationUrl, String paymentId) {}
