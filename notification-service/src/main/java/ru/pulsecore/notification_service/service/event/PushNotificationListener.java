package ru.pulsecore.notification_service.service.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.push.WebPushService;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.util.JsonUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushNotificationListener {

    private  final WebPushService webPushService;


    @KafkaListener(topics = KafkaTopics.PUSH_NOTIFICATION,batch = "true")
    public void sendPush(List<PushNotificationEvent> notificationEvents) {
        log.info("ПОЛУЧЕН БАТЧ: {} писем", notificationEvents.size());
        for (var n : notificationEvents) {
            webPushService.sendToPlayer(
                    n.playerId(),
                    n.title(),
                    n.body(),
                    n.url()
            );

        }
    }
}
