package ru.pulsecore.notification_service.service.mail.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategy;
import ru.pulsecore.notification_service.service.mail.UniversalMailSender;

import ru.pulsecore.notification_service.service.mail.template.MailFormat;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.MailContext;
import ru.pulsecore.shared.context.TournamentResultContext;

@Component
@RequiredArgsConstructor
public class TournamentResultMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;

    @Override
    public String getType() {
        return MailTypes.TOURNAMENT_RESULT;
    }

    @Override
    public void send(MailContext ctx) {
        TournamentResultContext c = (TournamentResultContext) ctx;
        String text = "Ваш результат: " + c.result();
        mailSender.send(MailFormat.TEXT, c.to(), "Результаты турнира", text, null, null);
    }
}