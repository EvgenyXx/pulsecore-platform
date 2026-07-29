package ru.pulsecore.user_service.client.fallback;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.dto.tournament.request.SumRequest;
import ru.pulsecore.shared.dto.tournament.response.PriorityLeagueResponse;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;
import ru.pulsecore.user_service.client.TournamentClient;
import ru.pulsecore.user_service.service.cache.TournamentClientCache;

import java.util.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class TournanamentClientFallBackFactory implements FallbackFactory<TournamentClient> {

    private final TournamentClientCache clientCache;

    @Override
    public TournamentClient create(Throwable cause) {
        return new TournamentClient() {

            @Override
            public SumResponse getSumForReport(SumRequest request) {
                log.warn("Турнир сервис не доступен пропускам отправку ");
                    return SumResponse.builder()
                            .fallback(true)
                            .build();
            }

            @Override
            public List<PriorityLeagueResponse> updatePriorityLeague(Set<UUID> playerIds) {

                if (cause instanceof FeignException.ServiceUnavailable) {

                    log.warn("Турнир-сервис недоступен (503), отдаём кеш");
                    return clientCache.getPriorityLeagueAll()
                            .entrySet()
                            .stream()
                            .map(e -> new PriorityLeagueResponse(e.getKey(), e.getValue()))
                            .toList();
                }
                log.error("Критическая ошибка: {}", cause.getMessage());
                return List.of();
            }
        };
    }
}
