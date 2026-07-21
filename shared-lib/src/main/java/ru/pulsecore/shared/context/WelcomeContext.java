package ru.pulsecore.shared.context;

public record WelcomeContext(
        String to,
        String firstName
) implements MailContext {


}