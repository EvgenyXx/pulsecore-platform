package ru.pulsecore.tournaments.service.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.util.JsonUtils;
import ru.pulsecore.tournaments.domain.OutBoxEvent;
import ru.pulsecore.tournaments.persistence.repository.OutboxEventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;

    public void process() {
        List<OutBoxEvent> outboxEvents = outboxEventRepository.findBySentFalse();
        for (OutBoxEvent event : outboxEvents) {
            try {
                Class<?> type = getTypeForTopic(event.getTopic());
                Object obj = JsonUtils.fromJson(event.getPayload(), type);
                kafkaTemplate.send(event.getTopic(), obj);
                event.setSent(true);
                event.setSentAt(LocalDateTime.now());
                outboxEventRepository.save(event);
                log.info("Event sent to topic: {}", event.getTopic());
            } catch (Exception e) {
                event.setRetryCount(event.getRetryCount() + 1);
                outboxEventRepository.save(event);
                log.error("Error occurred while processing outbox event", e);
            }
        }
    }

    private Class<?> getTypeForTopic(String topic) {
        if (KafkaTopics.EMAIL_NOTIFICATION.equals(topic)) {
            return MailNotificationEvent.class;
        }
        if (KafkaTopics.PUSH_NOTIFICATION.equals(topic)) {
            return PushNotificationEvent.class;
        }
        throw new IllegalStateException("Unknown topic: " + topic);
    }
}