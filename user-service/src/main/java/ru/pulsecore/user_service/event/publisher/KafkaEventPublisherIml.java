package ru.pulsecore.user_service.event.publisher;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherIml implements KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(String topic,Object event) {
        kafkaTemplate.send(topic, event);
        log.debug("Sent event {}", event);
    }


}