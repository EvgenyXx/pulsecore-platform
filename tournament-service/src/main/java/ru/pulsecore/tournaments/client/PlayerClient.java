package ru.pulsecore.tournaments.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pulsecore.shared.config.constants.feighn.FeignClientConstants;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;
import ru.pulsecore.shared.dto.player.PlayerData;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.shared.dto.player.PlayerSettingsDto;
import ru.pulsecore.tournaments.config.PlayerServiceFeignConfig;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = FeignClientConstants.USER_SERVICE,
        configuration = PlayerServiceFeignConfig.class

)
public interface PlayerClient {

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.SEARCH)
    List<PlayerSearchResponse> searchByName(@RequestParam String q);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.ID_BY_NAME)
    UUID getIdByFullName(@PathVariable String name);


    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.SETTINGS)
    PlayerSettingsDto getSettings(@PathVariable UUID playerId);


    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_LIVE_HALLS)
    String getLiveSelectedHalls(@PathVariable UUID playerId);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.ALL_ACTIVE)
    List<PlayerData> getAllActivePlayers();

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_HALLS)
    String getSelectedHalls(@PathVariable UUID playerId);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_NAME_BY_ID)
    String getPlayerName(@PathVariable UUID playerId);


    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_BY_ID)
    PlayerData getById(@PathVariable UUID playerId);


}
