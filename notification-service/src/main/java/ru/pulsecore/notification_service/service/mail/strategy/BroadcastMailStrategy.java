package ru.pulsecore.notification_service.service.mail.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategy;

import ru.pulsecore.notification_service.service.mail.UniversalMailSender;

import ru.pulsecore.notification_service.service.mail.template.MailFormat;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.BroadcastContext;
import ru.pulsecore.shared.context.MailContext;

@Component
@RequiredArgsConstructor
public class BroadcastMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;

    @Override
    public String getType() {
        return MailTypes.BROADCAST;
    }

    @Override
    public void send(MailContext ctx) {
        BroadcastContext c = (BroadcastContext) ctx;
        mailSender.send(MailFormat.TEXT, c.to(), "PulseCore — Уведомление", c.text(), null, null);
    }
}