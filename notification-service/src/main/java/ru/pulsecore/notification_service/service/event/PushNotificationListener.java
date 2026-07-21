package ru.pulsecore.notification_service.service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.push.WebPushService;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushNotificationListener {

    private  final WebPushService webPushService;


    @KafkaListener(topics = KafkaTopics.PUSH_NOTIFICATION)
    public void sendPush(PushNotificationEvent event) {
        webPushService.sendToPlayer(
                event.playerId(),
                event.title(),
                event.body(),
                event.url()
        );
        log.debug("Push notification received: {}", event);
    }
}
