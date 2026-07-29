package ru.pulsecore.tournaments.service.cache;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.dto.player.PlayerData;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayerCache {

    public final RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVE_PLAYERS = "cache:active-players";
    private static final String ROSTER_PLAYERS = "cache:roster-players";


    public void updateActivePlayers(List<PlayerData> activePlayers) {
        if (!activePlayers.isEmpty()) {
            redisTemplate.opsForValue().set(ACTIVE_PLAYERS, activePlayers);
        }
    }

    public List<PlayerData> getActivePlayers() {
        return (List<PlayerData>) redisTemplate.opsForValue().get(ACTIVE_PLAYERS);
    }


    public void updateRosterPlayers(String url,List<PlayerData> rosterPlayers) {

        String key = ROSTER_PLAYERS + rosterPlayers.stream()
                .map(playerData -> playerData.id().toString())
                .sorted()
                .collect(Collectors.joining());

        if (!rosterPlayers.isEmpty()) {
            redisTemplate.opsForValue().set(url, rosterPlayers, Duration.ofHours(3));
        }
    }


    public List<PlayerData> getRosterPlayers(String ulr) {
        return (List<PlayerData>) redisTemplate.opsForValue().get(ulr);

    }

}
