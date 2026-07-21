package ru.pulsecore.tournaments.service.finish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;
import ru.pulsecore.tournaments.persistence.repository.PlayerNotificationRepository;
import ru.pulsecore.tournaments.service.processor.TournamentProcessService;
import ru.pulsecore.tournaments.service.application.ResultService;
import ru.pulsecore.tournaments.domain.ParsedResult;
import ru.pulsecore.tournaments.domain.TournamentStatus;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TournamentFinishService {


    private final ResultService resultService;
    private final TournamentProcessService processService;
    private final PlayerNotificationRepository repo;
    private final TournamentRepository tournamentRepository;

    public boolean handleFinished(TournamentEntity t,
                                  List<PlayerNotification> notifications,
                                  Document doc,
                                  String playerName) {

        ParsedResult parsed = resultService.calculateAll(doc);
        if (parsed.status() != TournamentStatus.FINISHED) return false;
        processService.processTournament(notifications, parsed, playerName);
        t.setFinished(true);
        t.setProcessed(true);
        tournamentRepository.save(t);
        repo.saveAll(notifications);
        log.info("🏁 tournament finished: id={}, users={}, results={}",
                t.getExternalId(),
                notifications.size(),
                parsed.results().size());

        return true;
    }
}