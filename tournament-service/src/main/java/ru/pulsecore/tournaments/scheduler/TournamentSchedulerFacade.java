package ru.pulsecore.tournaments.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.tournaments.processor.TournamentFinishChecker;
import ru.pulsecore.tournaments.service.discovery.TournamentDiscoveryService;
import ru.pulsecore.tournaments.service.tournament.TournamentReminderService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TournamentSchedulerFacade {

    private final TournamentDiscoveryService tournamentDiscoveryService;
    private final TournamentFinishChecker tournamentFinishChecker;
    private final TournamentReminderService tournamentReminderService;


    public void chekNewTournaments() {
        tournamentDiscoveryService.checkNewTournaments();
    }


    public void processFinishedTournaments() {
        tournamentFinishChecker.processAll();
    }

    public void sendReminders() {
        tournamentReminderService.sendTournamentReminders();
    }




}
