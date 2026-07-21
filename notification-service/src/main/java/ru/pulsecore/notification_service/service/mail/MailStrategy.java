package ru.pulsecore.notification_service.service.mail;


import ru.pulsecore.shared.context.MailContext;

public interface MailStrategy {
    String getType();
    void send(MailContext context);
}