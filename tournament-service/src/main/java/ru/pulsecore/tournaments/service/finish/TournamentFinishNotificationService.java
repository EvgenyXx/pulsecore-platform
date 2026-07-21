package ru.pulsecore.tournaments.service.finish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.config.constants.KafkaTopics;

import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.tournaments.event.KafkaPublisher;
import ru.pulsecore.tournaments.service.notification.NotificationPermissionService;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.validator.PushMessageBuilder;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentFinishNotificationService {



    private final KafkaPublisher kafkaPublisher;
    private final NotificationPermissionService notificationPermissionService;

    public void sendCancelled(
            List<PlayerNotification> notifications) {
        for (PlayerNotification pn : notifications) {

            var tournament = pn.getTournament();

            log.info("❌ Tournament cancelled: player={}, tournament={}",
                    pn.getPlayerId(), tournament.getId());

            if (notificationPermissionService.canSendPush(pn.getPlayerId())) {
                String time = tournament.getTime() != null ? tournament.getTime() : "?";
                String date = tournament.getDate() != null ? tournament.getDate().toString() : "?";
                kafkaPublisher.publish(KafkaTopics.PUSH_NOTIFICATION,new PushNotificationEvent(
                        pn.getPlayerId(),
                        "❌ Турнир отменён!",
                        PushMessageBuilder.buildCancelledBody(date, time),
                        "/dashboard"
                ));
            }
        }
        log.debug("📩 Cancelled notifications: {}", notifications.size());
    }
}