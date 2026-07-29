package ru.pulsecore.user_service.service.cache;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TournamentClientCache {


    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private static final String PRIORITY_LEAGUE_ALL = "priority_league_all";


    public void savePriorityLeague(Map<UUID,String>data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            stringRedisTemplate.opsForValue().set(PRIORITY_LEAGUE_ALL, json, Duration.ofMinutes(30));
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации кеша лиг", e);
        }
    }

    public Map<UUID, String> getPriorityLeagueAll() {
        String json = stringRedisTemplate.opsForValue().get(PRIORITY_LEAGUE_ALL);
        if (json == null) return Map.of();
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory()
                    .constructMapType(Map.class, UUID.class, String.class));
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации кеша лиг", e);
            return Map.of();
        }
    }
}
