package ru.pulsecore.shared.context;

public record SubscriptionExpiringContext(String to, String name, String expiresAt) implements MailContext {}