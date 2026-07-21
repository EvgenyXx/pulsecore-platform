package ru.pulsecore.notification_service.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategyRegistry;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationListener {

    private final MailStrategyRegistry mailStrategyRegistry;

    @KafkaListener(topics = KafkaTopics.EMAIL_NOTIFICATION)
    public void handle(MailNotificationEvent event) {
        log.info("Получено email-событие: {}", event.getType());
        mailStrategyRegistry.send(event.getType(), event.getContext());
    }
}