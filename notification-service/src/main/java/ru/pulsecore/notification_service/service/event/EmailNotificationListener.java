package ru.pulsecore.notification_service.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategyRegistry;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;


import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationListener {

    private final MailStrategyRegistry mailStrategyRegistry;


    @KafkaListener(topics = KafkaTopics.EMAIL_NOTIFICATION,batch = "true")
    public void handle(List<MailNotificationEvent> mailNotificationEvents) {


        log.info("ПОЛУЧЕН БАТЧ: {} писем", mailNotificationEvents.size());

        for (var mail : mailNotificationEvents) {
            try {
                mailStrategyRegistry.send(mail.getType(), mail.getContext());
            } catch (Exception e) {
                log.error("Ошибка десериализации MailNotificationEvent: {}", e.getMessage(), e);

            }
        }
    }
}