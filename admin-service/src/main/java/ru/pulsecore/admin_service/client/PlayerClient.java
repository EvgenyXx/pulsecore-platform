package ru.pulsecore.admin_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.player.*;
import ru.pulsecore.shared.config.constants.feighn.FeignClientConstants;
import ru.pulsecore.shared.config.constants.feighn.FeignPlayerApi;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = FeignClientConstants.USER_SERVICE
)
public interface PlayerClient {

    //todo переопредилть декодер что бы было видно ошибки
    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_BY_ID)
    PlayerData getById(@PathVariable UUID playerId);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.BROADCAST_RECIPIENTS)
    List<BroadcastRecipient> getBroadcastRecipients();

    @DeleteMapping(FeignPlayerApi.BASE + FeignPlayerApi.DELETE)
    void deletePlayer(@PathVariable UUID playerId);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.PAGE_VIEWS_STATS)
    List<PageViewStats> getPageViewStats(@RequestParam int days);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.PAGE_VIEWS_PLAYER_STATS)
    List<PlayerPageViewStats> getPlayerPageViewStats(@RequestParam int days);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.SEARCH)
    List<PlayerSearchResponse> searchPlayers(@RequestParam String q);


    @PostMapping(FeignPlayerApi.BASE + FeignPlayerApi.ACTIVATE_SUBSCRIPTION)
    void activateSubscription(@PathVariable UUID playerId, @RequestParam int days);

    @PostMapping(FeignPlayerApi.BASE + FeignPlayerApi.DEACTIVATE_SUBSCRIPTION)
    void deactivateSubscription(@PathVariable UUID playerId);


    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_SUBSCRIPTION)
    SubscriptionStatusResponse getSubscription(@PathVariable UUID playerId);


    @PostMapping(FeignPlayerApi.BASE + FeignPlayerApi.GRANT_ROLE)
    void grantRole(@PathVariable UUID playerId, @RequestBody RoleRequest request);

    @PostMapping(FeignPlayerApi.BASE + FeignPlayerApi.REVOKE_ROLE)
    void revokeRole(@PathVariable UUID playerId, @RequestBody RoleRequest request);

    @GetMapping(FeignPlayerApi.BASE + FeignPlayerApi.GET_ROLES)
    List<String> getRoles(@PathVariable UUID playerId);


}
