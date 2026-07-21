package ru.pulsecore.tournaments.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publish(String topic, Object object) {
        kafkaTemplate.send(topic, object);
        log.debug("Published to topic: {}", topic);
    }
}
