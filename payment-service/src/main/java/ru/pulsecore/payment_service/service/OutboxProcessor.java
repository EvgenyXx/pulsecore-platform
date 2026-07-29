package ru.pulsecore.payment_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pulsecore.payment_service.domain.OutBoxEvent;
import ru.pulsecore.payment_service.repository.OutboxEventRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;


    public void process() {
        List<OutBoxEvent> outboxEvents = outboxEventRepository.findBySentFalse();
        for (OutBoxEvent event : outboxEvents) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getPayload());
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
}
