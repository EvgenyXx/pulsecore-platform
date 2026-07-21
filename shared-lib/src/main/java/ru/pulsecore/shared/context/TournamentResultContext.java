package ru.pulsecore.shared.context;

public record TournamentResultContext(
        String to,
        String result
) implements MailContext {


}