package ru.pulsecore.notification_service.service.mail.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategy;
import ru.pulsecore.notification_service.service.mail.MailTemplateService;

import ru.pulsecore.notification_service.service.mail.UniversalMailSender;

import ru.pulsecore.notification_service.service.mail.template.MailFormat;
import ru.pulsecore.notification_service.service.mail.template.MailTemplate;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.AdminNewUserContext;
import ru.pulsecore.shared.context.MailContext;

@Component
@RequiredArgsConstructor
public class AdminNewUserMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.ADMIN_NEW_USER;
    }

    @Override
    public void send(MailContext ctx) {
        AdminNewUserContext c = (AdminNewUserContext) ctx;
        String text = templates.format(MailTemplate.ADMIN_NEW_USER,
                c.name(), c.email(), c.ip(), c.device(),
                c.os(), c.browser(), c.userAgent());
        mailSender.send(MailFormat.TEXT, c.to(), "PulseCore — Новый игрок", text, null, null);
    }
}