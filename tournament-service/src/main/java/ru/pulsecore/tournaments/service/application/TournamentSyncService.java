package ru.pulsecore.tournaments.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.domain.ParsedResult;
import ru.pulsecore.tournaments.mapper.TournamentStatusMapper;
import ru.pulsecore.tournaments.persistence.entity.TournamentEntity;
import ru.pulsecore.tournaments.persistence.repository.TournamentRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TournamentSyncService {

    private final TournamentRepository tournamentRepository;
    private final TournamentStatusMapper statusMapper;

    public TournamentEntity sync(ParsedResult parsed, String link) {
        TournamentEntity t = tournamentRepository
                .findByExternalId(parsed.tournamentId())
                .orElseGet(TournamentEntity::new);

        t.setExternalId(parsed.tournamentId());
        t.setLink(link);


       statusMapper.apply(t,parsed.status());


        if (!parsed.results().isEmpty()) {
            t.setDate(LocalDate.parse(parsed.results().get(0).getDate()));
        }

        return tournamentRepository.save(t);
    }
}