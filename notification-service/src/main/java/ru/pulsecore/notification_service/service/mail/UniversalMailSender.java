package ru.pulsecore.notification_service.service.mail;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.config.AsyncConfig;
import ru.pulsecore.notification_service.properties.MailProperties;

import ru.pulsecore.notification_service.service.mail.sender.MailSendStrategy;
import ru.pulsecore.notification_service.service.mail.sender.PdfMailSender;
import ru.pulsecore.notification_service.service.mail.sender.TextMailSender;
import ru.pulsecore.notification_service.service.mail.template.MailFormat;


import java.util.Map;

@Slf4j
@Component
public class UniversalMailSender {

    private final Map<MailFormat, MailSendStrategy> senders;
    private final MailProperties props;


    public UniversalMailSender(TextMailSender text, PdfMailSender pdf, MailProperties props) {
        this.props = props;
        this.senders = Map.of(
                MailFormat.TEXT, text,
                MailFormat.PDF, pdf
        );
    }

    @Async(AsyncConfig.MAIL_EXECUTOR)
    public void send(MailFormat format,
                     String to, String subject,
                     String text, String fileName,
                     byte[] attachment) {
        log.info("Отправка почты в потоке: {}", Thread.currentThread().getName());
        senders.get(format).send(props.getFrom(),to, subject, text, fileName, attachment);
    }
}