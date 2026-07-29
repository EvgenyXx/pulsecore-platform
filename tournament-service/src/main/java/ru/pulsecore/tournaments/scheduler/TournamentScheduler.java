package ru.pulsecore.tournaments.scheduler;


import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TournamentScheduler {

    private final TournamentSchedulerFacade facade;

    @Scheduled(fixedDelay = 900_000)
    public void checkNewTournaments() {
        facade.chekNewTournaments();
    }

    @Scheduled(fixedRate = 420_000)
    public void processFinishedTournaments() {
        facade.processFinishedTournaments();
    }

    @Scheduled(fixedRate = 60_000)
    public void sendReminders() {
        facade.sendReminders();
    }
}