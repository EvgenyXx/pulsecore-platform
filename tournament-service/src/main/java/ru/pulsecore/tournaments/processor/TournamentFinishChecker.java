package ru.pulsecore.tournaments.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.service.finish.TournamentFinishService;
import ru.pulsecore.tournaments.service.parser.DocumentLoader;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.service.finish.TournamentFinishNotificationService;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import ru.pulsecore.tournaments.domain.TournamentStatus;
import ru.pulsecore.tournaments.service.parser.TournamentStatusParser;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentFinishChecker {

    private final DocumentLoader documentLoader;
    private final TournamentFinishService finishService;
    private final TournamentFinishNotificationService notificationService;
    private final PlayerNotificationRepository repo;
    private final TournamentRepository tournamentRepository;
    private final TournamentStatusParser tournamentStatusParser;

    public void processAll() {
        List<PlayerNotification> all = repo.findNotFinishedFull();
        if (all.isEmpty()) return;

        Map<String, List<PlayerNotification>> grouped = all.stream()
                .filter(p -> p.getTournament() != null)
                .collect(Collectors.groupingBy(p -> p.getTournament().getLink()));

        for (var entry : grouped.entrySet()) {
            process(entry.getKey(), entry.getValue());
        }
    }

    private void process(String link, List<PlayerNotification> notifications) {
        TournamentEntity t = getTournament(notifications);
        if (t == null || t.isProcessed()) return;

        Document doc = documentLoader.load(link);
        processByStatus(t, notifications, doc);
    }

    private TournamentEntity getTournament(List<PlayerNotification> notifications) {
        return notifications.stream()
                .map(PlayerNotification::getTournament)
                .findFirst()
                .orElse(null);
    }

    private void processByStatus(TournamentEntity t, List<PlayerNotification> notifications, Document doc) {
        TournamentStatus status = tournamentStatusParser.parseStatus(doc);

        if (status == TournamentStatus.CANCELLED) {
            handleCancelled(t, notifications);
            return;
        }

        if (!tournamentRepository.existsById(t.getId())) {
            log.warn("⚠️ Турнир {} (ID={}) не найден в БД, пропускаем обработку", t.getExternalId(), t.getId());
            return;
        }

        finishService.handleFinished(t, notifications, doc);
    }

    private void handleCancelled(TournamentEntity t, List<PlayerNotification> notifications) {
        if (t.isCancelled()) return;

        t.setCancelled(true);
        t.setProcessed(true);
        tournamentRepository.save(t);

        notificationService.sendCancelled(notifications);
        repo.saveAll(notifications);

        log.info("❌ tournament cancelled: id={}, users={}", t.getExternalId(), notifications.size());
    }
}