package ru.pulsecore.tournaments.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pulsecore.tournaments.service.outbox.OutboxProcessor;

@Component
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final OutboxProcessor outboxProcessor;


    @Scheduled(fixedDelayString = "PT5S")
    public void outboxEvent() {
        outboxProcessor.process();
    }
}
