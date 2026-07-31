package ru.pulsecore.tournaments.service.tournament;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import ru.pulsecore.tournaments.service.notification.NotificationPermissionService;
import ru.pulsecore.tournaments.service.outbox.OutBoxService;
import ru.pulsecore.tournaments.validator.PushMessageBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TournamentReminderService {
    //todo добавить батчу отправлять пачка и так же сделать в получателе
    private final PlayerNotificationRepository notificationRepository;
    private final OutBoxService outBoxService;
    private final NotificationPermissionService notificationPermissionService;

    @Transactional
    public void sendTournamentReminders() {
        List<PlayerNotification> pending = notificationRepository.findPendingWithTournament();
        if (pending.isEmpty()) return;

        Set<UUID> allPlayerIds = pending.stream()
                .map(PlayerNotification::getPlayerId)
                .collect(Collectors.toSet());

        Map<UUID, Boolean> canPush = notificationPermissionService.canSendPush(allPlayerIds);

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        List<Object>batch = new ArrayList<>();
        pending.forEach(pn -> {
            if (!canPush.getOrDefault(pn.getPlayerId(), false)) return;
            processNotification(pn, today, tomorrow, now,batch);
        });

        if (!batch.isEmpty()) {
            outBoxService.saveAll(KafkaTopics.PUSH_NOTIFICATION, batch);
        }
    }

    private void processNotification(
            PlayerNotification pn,
            LocalDate today,
            LocalDate tomorrow,
            LocalTime now,
            List<Object>batch) {
        var tournament = pn.getTournament();
        if (tournament == null || tournament.getDate() == null) return;

        if (tournament.getDate().equals(today)) {
            sendHourReminder(pn, now,batch);
        }
        if (tournament.getDate().equals(tomorrow)) {
            sendEveningReminder(pn, now,batch);
        }
    }

    private void sendHourReminder(PlayerNotification pn, LocalTime now, List<Object> batch) {
        String time = pn.getTournament().getTime();
        if (time == null || time.isEmpty()) return;

        Long minutes = parseMinutesUntil(time, now);
        if (minutes == null || minutes <= 0 || minutes > 60) return;

        batch.add(new PushNotificationEvent(
                pn.getPlayerId(),
                "🏆 Турнир начинается!",
                PushMessageBuilder.buildHourReminderBody(time, minutes),
                "/dashboard"
        ));
        pn.setPushReminderSent(true);
        notificationRepository.save(pn);
    }

    private Long parseMinutesUntil(String time, LocalTime now) {
        try {
            LocalTime tournamentTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return java.time.Duration.between(now, tournamentTime).toMinutes();
        } catch (Exception e) {
            log.warn("Ошибка парсинга времени турнира: {}", time);
            return null;
        }
    }

    private void sendEveningReminder(PlayerNotification pn, LocalTime now, List<Object> batch) {
        if (now.getHour() != 20 || pn.isPushEveningSent()) return;

        batch.add(new PushNotificationEvent(
                pn.getPlayerId(),
                "📅 Завтра турнир!",
                PushMessageBuilder.buildEveningReminderBody(pn.getTournament().getTime()),
                "/dashboard"
        ));
        pn.setPushEveningSent(true);
        notificationRepository.save(pn);
    }
}