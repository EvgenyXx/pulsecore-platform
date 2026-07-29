package ru.pulsecore.tournaments.event;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.shared.util.JsonUtils;
import ru.pulsecore.tournaments.service.PlayerCreatedHandler;


@Component
@RequiredArgsConstructor
@Slf4j
public class PlayerCreatedListener {

//todo добавь обработку если все пошло не по плану
    private final PlayerCreatedHandler  playerCreatedHandler;

    @KafkaListener(topics = KafkaTopics.PLAYER_CREATED)
    public void handle(String json) {
        PlayerCreatedEvent event = JsonUtils.fromJson(json, PlayerCreatedEvent.class);
        playerCreatedHandler.handle(event);
        log.info("Player {} created", event.playerId());
    }

}