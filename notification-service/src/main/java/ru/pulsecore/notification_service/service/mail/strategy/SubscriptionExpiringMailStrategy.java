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
import ru.pulsecore.shared.context.SubscriptionExpiringContext;


@RequiredArgsConstructor
@Component
public class SubscriptionExpiringMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;
    @Override
    public String getType() {
        return MailTypes.SUBSCRIPTION_EXPIRING;
    }

    @Override
    public void send(MailContext ctx) {
        SubscriptionExpiringContext c = (SubscriptionExpiringContext) ctx;
        String text = templates.format(MailTemplate.SUBSCRIPTION_EXPIRING, c.name(), c.expiresAt());
        mailSender.send(MailFormat.TEXT, c.to(), "⏳ Подписка заканчивается", text, null, null);
    }
}