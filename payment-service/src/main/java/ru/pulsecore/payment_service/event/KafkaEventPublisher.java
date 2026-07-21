package ru.pulsecore.payment_service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void  publish(String topic, Object payload) {
        log.info("Publishing event {} to topic {}", payload, topic);
        kafkaTemplate.send(topic, payload);
    }
}
