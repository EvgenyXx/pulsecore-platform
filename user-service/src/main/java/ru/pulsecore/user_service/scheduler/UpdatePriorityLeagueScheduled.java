package ru.pulsecore.user_service.scheduler;


import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.pulsecore.user_service.service.player.UpdatePriorityLeagueService;

@Service
@RequiredArgsConstructor
public class UpdatePriorityLeagueScheduled {

    private final UpdatePriorityLeagueService updatePriorityLeagueService;


    @Scheduled(initialDelay = 0, fixedRate = 900_000)
    public void updatePriorityLeague() {
        updatePriorityLeagueService.updatePriorityLeague();
    }
}
