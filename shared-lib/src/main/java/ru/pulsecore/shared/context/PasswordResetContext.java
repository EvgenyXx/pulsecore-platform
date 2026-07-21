package ru.pulsecore.shared.context;

public record PasswordResetContext(
        String to,
        String code
) implements MailContext {


}