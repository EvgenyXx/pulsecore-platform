package ru.pulsecore.notification_service.service.mail.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategy;
import ru.pulsecore.notification_service.service.mail.MailTemplateService;
import ru.pulsecore.notification_service.service.mail.UniversalMailSender;

import ru.pulsecore.notification_service.service.mail.template.MailFormat;
import ru.pulsecore.notification_service.service.mail.template.MailTemplate;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.MailContext;
import ru.pulsecore.shared.context.VerificationContext;

@Component
@RequiredArgsConstructor
public class VerificationMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.VERIFICATION;
    }

    @Override
    public void send(MailContext ctx) {
        VerificationContext c = (VerificationContext) ctx;
        String text = templates.format(MailTemplate.VERIFICATION, c.code());
        mailSender.send(MailFormat.TEXT, c.to(), "PulseCore — Код подтверждения", text, null, null);
    }
}