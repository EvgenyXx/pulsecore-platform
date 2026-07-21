package ru.pulsecore.shared.context;

public record VerificationContext(
        String to,
        String code
) implements MailContext {


}