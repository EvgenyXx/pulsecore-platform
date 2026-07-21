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
import ru.pulsecore.shared.context.WelcomeContext;

@Component
@RequiredArgsConstructor
public class WelcomeMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.WELCOME;
    }

    @Override
    public void send(MailContext ctx) {
        WelcomeContext c = (WelcomeContext) ctx;
        String text = templates.format(MailTemplate.WELCOME, c.firstName());
        mailSender.send(
                MailFormat.TEXT,
                c.to(),
                "🏓 " + c.firstName() + ", добро пожаловать в PulseCore!",
                text,
                null,
                null);
    }
}