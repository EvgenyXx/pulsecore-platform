package ru.pulsecore.tournaments.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.service.parser.DocumentLoader;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.service.finish.TournamentFinishNotificationService;
import ru.pulsecore.tournaments.service.finish.TournamentFinishService;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import ru.pulsecore.tournaments.domain.TournamentStatus;
import ru.pulsecore.tournaments.service.parser.TournamentStatusParser;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentFinishProcessor {

    private final DocumentLoader documentLoader;
    private final TournamentFinishService finishService;
    private final TournamentFinishNotificationService notificationService;
    private final PlayerNotificationRepository repo;
    private final TournamentRepository tournamentRepository;
    private final TournamentStatusParser tournamentStatusParser;

    public Result process(String link,
                          List<PlayerNotification> notifications,
                          String playerName
    ) {
        if (link == null || notifications == null || notifications.isEmpty()) return null;

        TournamentEntity t = getTournament(notifications);
        if (t == null || t.isProcessed()) return null;

        Document doc = documentLoader.load(link);
        return processByStatus(t, notifications, doc, playerName);
    }

    private TournamentEntity getTournament(List<PlayerNotification> notifications) {
        return notifications.stream()
                .map(PlayerNotification::getTournament)
                .findFirst()
                .orElse(null);
    }

    private Result processByStatus(TournamentEntity t,
                                   List<PlayerNotification> notifications,
                                   Document doc,
                                   String playerName) {
        TournamentStatus status = tournamentStatusParser.parseStatus(doc);

        if (status == TournamentStatus.CANCELLED) {
            return handleCancelled(t, notifications);
        }

        if (!tournamentRepository.existsById(t.getId())) {
            log.warn("⚠️ Турнир {} (ID={}) не найден в БД, пропускаем обработку", t.getExternalId(), t.getId());
            return null;
        }

        return finishService.handleFinished(t, notifications, doc, playerName) ? new Result(true, false) : null;
    }

    private Result handleCancelled(TournamentEntity t, List<PlayerNotification> notifications) {
        if (t.isCancelled()) return new Result(false, true);

        t.setCancelled(true);
        t.setProcessed(true);
        tournamentRepository.save(t);

        notificationService.sendCancelled(notifications);
        repo.saveAll(notifications);

        log.info("❌ tournament cancelled: id={}, users={}", t.getExternalId(), notifications.size());
        return new Result(false, true);
    }

    public record Result(boolean finished, boolean cancelled) {
    }
}