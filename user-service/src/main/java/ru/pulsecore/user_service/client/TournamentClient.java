package ru.pulsecore.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import ru.pulsecore.shared.config.constants.feighn.FeignClientConstants;
import ru.pulsecore.shared.config.constants.feighn.FeignTournamentApi;

import ru.pulsecore.shared.dto.tournament.request.SumRequest;
import ru.pulsecore.shared.dto.tournament.response.PriorityLeagueResponse;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;
import ru.pulsecore.user_service.client.fallback.TournanamentClientFallBackFactory;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@FeignClient(
        name = FeignClientConstants.TOURNAMENT_SERVICE,
        fallbackFactory = TournanamentClientFallBackFactory.class
)
public interface TournamentClient {

    @PostMapping(FeignTournamentApi.BASE + FeignTournamentApi.SUM)
    SumResponse getSumForReport(@RequestBody SumRequest request);

    @GetMapping(FeignTournamentApi.BASE + FeignTournamentApi.UPDATE_PRIMARY_LEAGUE)
    List<PriorityLeagueResponse> updatePriorityLeague(@RequestParam Set<UUID> playerIds);


}