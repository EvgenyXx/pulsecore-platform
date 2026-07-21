package ru.pulsecore.notification_service.service.mail.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategy;
import ru.pulsecore.notification_service.service.mail.MailTemplateService;

import ru.pulsecore.notification_service.service.mail.UniversalMailSender;

import ru.pulsecore.notification_service.service.mail.template.MailFormat;
import ru.pulsecore.notification_service.service.mail.template.MailTemplate;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.AdminPaymentContext;
import ru.pulsecore.shared.context.MailContext;

@Component
@RequiredArgsConstructor
public class AdminPaymentMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.ADMIN_PAYMENT_RECEIVED;
    }

    @Override
    public void send(MailContext ctx) {
        AdminPaymentContext c = (AdminPaymentContext) ctx;
        String text = templates.format(MailTemplate.ADMIN_PAYMENT,
                c.playerName(), c.months(), c.amount(), c.currency());
        mailSender.send(MailFormat.TEXT, c.to(), "PulseCore — Новая оплата", text, null, null);
    }
}