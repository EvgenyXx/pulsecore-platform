package ru.pulsecore.shared.context;

public record AdminNewUserContext(
        String to,
        String name,
        String email,
        String ip,
        String device,
        String os,
        String browser,
        String userAgent
) implements MailContext {


}