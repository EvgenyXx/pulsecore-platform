package ru.pulsecore.shared.context;

public record BroadcastContext(
        String to,
        String text
) implements MailContext {


}