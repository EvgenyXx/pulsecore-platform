package ru.pulsecore.user_service.service.player;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.dto.tournament.response.PriorityLeagueResponse;
import ru.pulsecore.user_service.client.TournamentClient;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.repository.PlayerRepository;
import ru.pulsecore.user_service.service.cache.TournamentClientCache;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePriorityLeagueService {

    private final TournamentClient tournamentClient;
    private final PlayerRepository playerRepository;
    private final TournamentClientCache tournamentClientCache;

    @Transactional
    public  void updatePriorityLeague() {

        List<Player>players = playerRepository.findAll();
        Set<UUID> playerIds = players.stream().map(Player::getId).collect(Collectors.toSet());

        Map<UUID,String>leagueMap = tournamentClient.updatePriorityLeague(playerIds)
                .stream().collect(Collectors.toMap(
                        PriorityLeagueResponse::playerId,
                        PriorityLeagueResponse::priorityLeague
        ));
        tournamentClientCache.savePriorityLeague(leagueMap);
        players.forEach(player -> {
            Optional.ofNullable(leagueMap.get(player.getId())).ifPresent(player::setPrimaryLeague);
        });
        log.info("Обновление лиг прошло успешно");

    }
}
