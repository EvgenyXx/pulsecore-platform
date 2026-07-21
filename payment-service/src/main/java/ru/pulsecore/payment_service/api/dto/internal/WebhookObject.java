package ru.pulsecore.payment_service.api.dto.internal;

import java.util.Map;

public record WebhookObject(String id, Amount amount, Map<String, String> metadata) {}