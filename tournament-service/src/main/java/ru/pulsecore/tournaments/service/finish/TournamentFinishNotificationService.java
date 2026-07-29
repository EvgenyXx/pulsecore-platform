package ru.pulsecore.tournaments.service.finish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.tournaments.service.notification.NotificationPermissionService;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.service.outbox.OutBoxService;
import ru.pulsecore.tournaments.validator.PushMessageBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentFinishNotificationService {
    //todo добавить батчу отправлять пачка и так же сделать в получателе
    private final OutBoxService outBoxService;
    private final NotificationPermissionService notificationPermissionService;

    public void sendCancelled(List<PlayerNotification> notifications) {
        if (notifications.isEmpty()) return;

        Set<UUID> playerIds = notifications.stream()
                .map(PlayerNotification::getPlayerId)
                .collect(Collectors.toSet());

        Map<UUID, Boolean> canPushMap = notificationPermissionService.canSendPush(playerIds);

        for (PlayerNotification pn : notifications) {
            if (!canPushMap.getOrDefault(pn.getPlayerId(), false)) continue;

            var tournament = pn.getTournament();
            log.info("❌ Tournament cancelled: player={}, tournament={}",
                    pn.getPlayerId(), tournament.getId());

            String time = tournament.getTime() != null ? tournament.getTime() : "?";
            String date = tournament.getDate() != null ? tournament.getDate().toString() : "?";
            outBoxService.save(KafkaTopics.PUSH_NOTIFICATION, new PushNotificationEvent(
                    pn.getPlayerId(),
                    "❌ Турнир отменён!",
                    PushMessageBuilder.buildCancelledBody(date, time),
                    "/dashboard"
            ));
        }
        log.debug("📩 Cancelled notifications: {}", notifications.size());
    }
}