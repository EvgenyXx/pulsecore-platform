package ru.pulsecore.tournaments.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.util.FeignUtils;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.service.discovery.TournamentDiscoveryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {
    //todo добавить в клиента фолбек

    private final TournamentDiscoveryService discoveryService;
    private final PlayerClient playerClient;

    @Scheduled(fixedDelay = 900000)
    public void checkAllUsers() {
        FeignUtils.tryExecute(
                playerClient::getAllActivePlayers,
                "user-service"
        ).ifPresentOrElse(
                players -> players.forEach(playerData ->
                        discoveryService.checkNewTournaments(
                                playerData.id(), playerData.email(), playerData.name())
                ),
                () -> log.warn("Пропущена проверка — user-service недоступен")
        );
    }
}