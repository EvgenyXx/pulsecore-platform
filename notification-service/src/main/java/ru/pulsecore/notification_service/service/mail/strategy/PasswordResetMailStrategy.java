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
import ru.pulsecore.shared.context.PasswordResetContext;

@Component
@RequiredArgsConstructor
public class PasswordResetMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.PASSWORD_RESET;
    }

    @Override
    public void send(MailContext ctx) {
        PasswordResetContext c = (PasswordResetContext) ctx;
        String text = templates.format(MailTemplate.PASSWORD_RESET, c.code());
        mailSender.send(MailFormat.TEXT, c.to(), "PulseCore — Сброс пароля", text, null, null);
    }
}