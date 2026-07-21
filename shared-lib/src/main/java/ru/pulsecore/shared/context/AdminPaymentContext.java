package ru.pulsecore.shared.context;

public record AdminPaymentContext(
        String to,
        String playerName,
        int months,
        String amount,
        String currency
) implements MailContext {


}