package ru.pulsecore.user_service.event.publisher;



public interface KafkaEventPublisher {
    void publish(String topic,Object object);
}