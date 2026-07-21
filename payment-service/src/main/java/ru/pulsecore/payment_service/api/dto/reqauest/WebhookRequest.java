package ru.pulsecore.payment_service.api.dto.reqauest;

import ru.pulsecore.payment_service.api.dto.internal.WebhookObject;

public record WebhookRequest(String event, WebhookObject object) {}
