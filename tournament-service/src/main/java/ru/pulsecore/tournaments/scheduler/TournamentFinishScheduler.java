package ru.pulsecore.tournaments.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.util.FeignUtils;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.processor.TournamentFinishProcessor;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TournamentFinishScheduler {

    private final PlayerNotificationRepository repo;
    private final TournamentFinishProcessor processor;
    private final PlayerClient playerClient;

    @Scheduled(fixedRate = 420000)
    public void checkFinished() {
        List<PlayerNotification> list = repo.findNotFinishedFull();
        if (list.isEmpty()) return;

        Map<String, List<PlayerNotification>> grouped = list.stream()
                .filter(p -> p.getTournament() != null)
                .collect(Collectors.groupingBy(p -> p.getTournament().getLink()));

        for (var entry : grouped.entrySet()) {
            String link = entry.getKey();
            List<PlayerNotification> notifications = entry.getValue();
            UUID playerId = notifications.get(0).getPlayerId();

            FeignUtils.tryExecute(
                    () -> playerClient.getById(playerId),
                    "user-service"
            ).ifPresentOrElse(
                    playerData -> processor.process(link, notifications, playerData.name()),
                    () -> log.warn("Пропущена обработка для playerId={} — user-service недоступен", playerId)
            );
        }
    }
}