package ru.pulsecore.tournaments.service.discovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.NewTournamentContext;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.shared.dto.tournament.TournamentDto;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.util.DateTimeUtils;
import ru.pulsecore.shared.util.StringUtils;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.service.cache.PlayerCache;
import ru.pulsecore.tournaments.service.notification.NotificationPermissionService;
import ru.pulsecore.tournaments.service.outbox.OutBoxService;
import ru.pulsecore.tournaments.validator.PushMessageBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentDiscoveryService {

    private final TournamentFinder finder;
    private final TournamentFilter filter;
    private final TournamentSaver saver;
    private final OutBoxService outBoxService;
    private final NotificationPermissionService notificationPermissionService;
    private final PlayerClient playerClient;
    private final PlayerCache playerCache;

    public void checkNewTournaments() {

        List<PlayerData>activePlayers = playerClient.getAllActivePlayers();
        playerCache.updateActivePlayers(activePlayers);
        if (activePlayers.isEmpty()) return;

        Set<String> playerNames = activePlayers.stream().map(PlayerData::name).collect(Collectors.toSet());
        Map<String, List<TournamentDto>> allFound = finder.find(playerNames);

        Set<UUID> ids = activePlayers.stream().map(PlayerData::id).collect(Collectors.toSet());
        Map<UUID, Boolean> canEmailMap = notificationPermissionService.canSendEmail(ids);
        Map<UUID, Boolean> canPushMap = notificationPermissionService.canSendPush(ids);

        for (PlayerData player : activePlayers) {
            List<TournamentDto> playerTournaments = allFound.getOrDefault(player.name(), List.of());
            List<TournamentDto> newTournaments = filter.findNew(player.id(), playerTournaments);
            if (newTournaments.isEmpty()) continue;

            saver.save(player.id(), newTournaments);

            boolean canEmail = canEmailMap.getOrDefault(player.id(), false);
            boolean canPush = canPushMap.getOrDefault(player.id(), false);

            for (TournamentDto t : newTournaments) {
                sendEmailIfAllowed(player, t, canEmail);
                sendPushIfAllowed(player, t, canPush);
            }
            log.info("Отправлены уведомления о {} турнирах для {}", newTournaments.size(), player.email());
        }
    }

    private void sendEmailIfAllowed(PlayerData player, TournamentDto t, boolean canEmail) {
        if (!canEmail) return;
        String rawDate = t.getDate() != null ? t.getDate().getDate() : null;
        outBoxService.save(KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(MailTypes.NEW_TOURNAMENT,
                        new NewTournamentContext(
                                player.email(),
                                StringUtils.extractFirstName(player.name()),
                                DateTimeUtils.formatDate(rawDate),
                                DateTimeUtils.formatTime(rawDate),
                                t.getHall(),
                                t.getLeague(),
                                t.getPlayers() != null ? String.join(", ", t.getPlayers()) : "",
                                t.getLink()
                        )));
    }

    private void sendPushIfAllowed(PlayerData player, TournamentDto t, boolean canPush) {
        if (!canPush) return;
        outBoxService.save(KafkaTopics.PUSH_NOTIFICATION,
                new PushNotificationEvent(player.id(), "📋 Вы в составе!",
                        PushMessageBuilder.buildNewTournamentBody(player.name(), t), "/dashboard"));
    }
}