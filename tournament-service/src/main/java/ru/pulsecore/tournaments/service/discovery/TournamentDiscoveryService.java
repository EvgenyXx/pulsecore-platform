package ru.pulsecore.tournaments.service.discovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.NewTournamentContext;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;

import ru.pulsecore.shared.util.DateTimeUtils;
import ru.pulsecore.shared.util.StringUtils;
import ru.pulsecore.tournaments.event.KafkaPublisher;
import ru.pulsecore.tournaments.service.notification.NotificationPermissionService;
import ru.pulsecore.tournaments.validator.PushMessageBuilder;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentDiscoveryService {


    private final TournamentFinder finder;
    private final TournamentFilter filter;
    private final TournamentSaver saver;
    private final KafkaPublisher kafkaPublisher;
    private final NotificationPermissionService notificationPermissionService;


    public void checkNewTournaments(UUID playerId, String email, String playerName) {
        List<TournamentDto> newTournaments = findNewTournaments(playerName, playerId);
        if (newTournaments.isEmpty()) return;

        saver.save(playerId, newTournaments);

        newTournaments.forEach(t -> {
            String rawDate = t.getDate() != null ? t.getDate().getDate() : null;

            if (notificationPermissionService.canSendEmail(playerId)) {
                kafkaPublisher.publish(KafkaTopics.EMAIL_NOTIFICATION,
                        new MailNotificationEvent(MailTypes.NEW_TOURNAMENT,
                                new NewTournamentContext(
                                        email,
                                        StringUtils.extractFirstName(playerName),
                                        DateTimeUtils.formatDate(rawDate),
                                        DateTimeUtils.formatTime(rawDate),
                                        t.getHall(),
                                        t.getLeague(),
                                        t.getPlayers() != null ? String.join(", ", t.getPlayers()) : "",
                                        t.getLink()
                                )
                        ));
            }

            if (notificationPermissionService.canSendPush(playerId)) {
                kafkaPublisher.publish(KafkaTopics.PUSH_NOTIFICATION,
                        new PushNotificationEvent(playerId, "📋 Вы в составе!",
                                PushMessageBuilder.buildNewTournamentBody(playerName, t), "/dashboard"));
            }
        });

        log.info("Отправлены уведомления о {} турнирах для {}", newTournaments.size(), email);
    }

    private List<TournamentDto> findNewTournaments(String playerName, UUID playerId) {
        List<TournamentDto> tournaments = finder.find(playerName);
        if (tournaments.isEmpty()) return List.of();
        return filter.findNew(playerId, tournaments);
    }
}