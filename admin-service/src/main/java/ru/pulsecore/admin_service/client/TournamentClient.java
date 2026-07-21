package ru.pulsecore.admin_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.config.constants.feighn.FeignClientConstants;
import ru.pulsecore.shared.config.constants.feighn.FeignTournamentApi;
import ru.pulsecore.shared.dto.tournament.response.AdminCalculateResponse;
import ru.pulsecore.shared.dto.tournament.request.CalculateRequest;

import java.util.UUID;

@FeignClient(
        name = FeignClientConstants.TOURNAMENT_SERVICE)
public interface TournamentClient {


    @DeleteMapping(FeignTournamentApi.BASE + FeignTournamentApi.PLAYER_TOURNAMENTS)
    int deletePlayerTournaments(@PathVariable UUID playerId);

    @PostMapping(FeignTournamentApi.BASE + FeignTournamentApi.PLAYER_TOURNAMENTS_RESYNC)
    void resyncPlayerTournaments(@PathVariable UUID playerId, @RequestParam String playerName);

    @PostMapping(FeignTournamentApi.BASE  + FeignTournamentApi.CALCULATE)
    AdminCalculateResponse calculate(@RequestBody CalculateRequest request) ;
}