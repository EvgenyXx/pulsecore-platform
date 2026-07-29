package ru.pulsecore.notification_service.service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.PlayerCreatedHandler;

import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.shared.util.JsonUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayerCreatedListener {

    private final PlayerCreatedHandler  playerCreatedHandler;


    @KafkaListener(topics = KafkaTopics.PLAYER_CREATED)
    public void handle(String json) {
       try {
           PlayerCreatedEvent event = JsonUtils.fromJson(json,PlayerCreatedEvent.class);
           playerCreatedHandler.process(event);
       }catch (Exception e) {
           log.error("Ошибка обработки PlayerCreatedEvent: {}", e.getMessage(), e);
       }
    }
}