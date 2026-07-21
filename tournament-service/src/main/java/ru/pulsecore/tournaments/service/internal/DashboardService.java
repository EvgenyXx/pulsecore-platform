package ru.pulsecore.tournaments.service.internal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.dto.tournament.response.LastResultDto;
import ru.pulsecore.shared.dto.tournament.response.UpcomingLineupDto;
import ru.pulsecore.shared.util.StringUtils;
import ru.pulsecore.tournaments.persistence.entity.Lineup;
import ru.pulsecore.tournaments.persistence.repository.LineupRepository;
import ru.pulsecore.tournaments.persistence.repository.TournamentResultRepository;



import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DashboardService {

    private final TournamentResultRepository tournamentResultRepository;
    private final LineupRepository lineupRepository;





    public LastResultDto getLastResult(UUID playerId) {
        return tournamentResultRepository.findLastResult(playerId)
                .map(l -> LastResultDto.builder()
                        .amount(l.getAmount())
                        .date(l.getDate().toString())
                        .tournamentLink(l.getTournamentLink())
                        .build()
                ).orElse(null);
    }

    public List<UpcomingLineupDto> getUpcomingLineups(String playerName) {
        String playerNameLower = playerName.toLowerCase();
        LocalDate today = LocalDate.now();
        List<Lineup> lineups = lineupRepository.findByDateBetweenOrderByDateAscTimeAsc(today, today.plusDays(2));

        Map<LocalDate, List<Lineup>> byDate = lineups.stream()
                .collect(Collectors.groupingBy(Lineup::getDate, LinkedHashMap::new, Collectors.toList()));
        LocalDate soonestDate = byDate.keySet().stream().min(LocalDate::compareTo).orElse(null);

        List<UpcomingLineupDto> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Lineup>> entry : byDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<Lineup> myLineups = entry.getValue().stream()
                    .filter(l -> l.getPlayers().toLowerCase().contains(playerNameLower))
                    .toList();

            if (myLineups.isEmpty()) {
                result.add(UpcomingLineupDto.builder()
                        .date(date.toString())
                        .inLineup(false)
                        .isSoon(date.equals(soonestDate))
                        .build());
            } else {
                myLineups.forEach(lineup -> result.add(UpcomingLineupDto.builder()
                        .date(lineup.getDate().toString())
                        .time(lineup.getTime())
                        .league(lineup.getLeague())
                        .inLineup(true)
                        .players(StringUtils.capitalize(lineup.getPlayers()))
                        .isSoon(date.equals(soonestDate))
                        .build()));
            }
        }
        return result;
    }

}
