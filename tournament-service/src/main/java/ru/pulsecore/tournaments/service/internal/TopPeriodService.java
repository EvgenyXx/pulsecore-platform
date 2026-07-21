package ru.pulsecore.tournaments.service.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.pulsecore.shared.config.CacheNames;
import ru.pulsecore.shared.dto.tournament.response.TopLeagueResponse;
import ru.pulsecore.shared.dto.tournament.response.TopPlayerDto;
import ru.pulsecore.tournaments.persistence.entity.TopPlayersView;
import ru.pulsecore.tournaments.persistence.repository.TopPlayersViewRepository;
import ru.pulsecore.tournaments.persistence.repository.TournamentResultRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopPeriodService {

    private final TopPlayersViewRepository repository;
    private final TournamentResultRepository tournamentResultRepository;

    @Cacheable(value = CacheNames.TOP_ALL, key = CacheNames.KEY_PERIOD)
    public TopLeagueResponse getTopAllLeagues(String period, UUID playerId) {
        List<TopPlayersView> all = repository.findByPeriodOrderByTotalDesc(period);
        String primaryLeague = tournamentResultRepository.findPrimaryLeague(playerId);
        return buildResponse(all, playerId, primaryLeague);
    }

    @Cacheable(value = CacheNames.TOP_LEAGUE, key = CacheNames.KEY_PERIOD_LEAGUE)
    public TopLeagueResponse getTopByLeague(String period,
                                            String league,
                                            UUID playerId) {
        String primaryLeague = tournamentResultRepository.findPrimaryLeague(playerId);

        if (!league.equals(primaryLeague)) {
            List<TopPlayersView> top5 = repository.findByPeriodAndPrimaryLeagueOrderByTotalDesc(period, league);
            if (top5.size() > 5) top5 = top5.subList(0, 5);
            return TopLeagueResponse.builder()
                    .top5(top5.stream().map(this::toDto).toList())
                    .playerPosition(0)
                    .playerTournaments(0)
                    .primaryLeague(primaryLeague)
                    .build();
        }

        List<TopPlayersView> all = repository.findByPeriodAndPrimaryLeagueOrderByTotalDesc(period, league);
        return buildResponse(all, playerId, primaryLeague);
    }

    private TopLeagueResponse buildResponse(List<TopPlayersView> all, UUID playerId, String primaryLeague) {
        List<TopPlayersView> top5 = all.size() > 5 ? all.subList(0, 5) : all;

        int position = 0;
        int tournaments = 0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPlayerId().equals(playerId)) {
                position = i + 1;
                tournaments = all.get(i).getTournaments().intValue();
                break;
            }
        }

        return TopLeagueResponse.builder()
                .top5(top5.stream().map(this::toDto).toList())
                .playerPosition(position)
                .playerTournaments(tournaments)
                .primaryLeague(primaryLeague)
                .build();
    }

    private TopPlayerDto toDto(TopPlayersView v) {
        return TopPlayerDto.builder()
                .name(v.getName())
                .tournaments(v.getTournaments())
                .build();
    }
}