package ru.pulsecore.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.pulsecore.shared.config.constants.feighn.FeignClientConstants;
import ru.pulsecore.shared.config.constants.feighn.FeignTournamentApi;

import ru.pulsecore.shared.dto.tournament.request.SumRequest;
import ru.pulsecore.shared.dto.tournament.response.SumResponse;



@FeignClient(
        name = FeignClientConstants.TOURNAMENT_SERVICE
)
public interface TournamentClient {

    @PostMapping(FeignTournamentApi.BASE + FeignTournamentApi.SUM)
    SumResponse getSumForReport(@RequestBody SumRequest request);


}