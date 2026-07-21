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
import ru.pulsecore.shared.context.NewTournamentContext;

@Component
@RequiredArgsConstructor
public class NewTournamentMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.NEW_TOURNAMENT;
    }

    @Override
    public void send(MailContext ctx) {
        NewTournamentContext c = (NewTournamentContext) ctx;
        String text = templates.format(MailTemplate.NEW_TOURNAMENT,
                c.firstName(), c.date(), c.time(), c.hall(),
                c.league(), c.players(), c.link());
        mailSender.send(
                MailFormat.TEXT,
                c.to(),
                "🏓 " + c.firstName() + ", новый турнир",
                text,
                null,
                null);
    }
}