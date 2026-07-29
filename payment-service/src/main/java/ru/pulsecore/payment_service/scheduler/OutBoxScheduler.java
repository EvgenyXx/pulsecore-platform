package ru.pulsecore.payment_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pulsecore.payment_service.service.OutboxProcessor;


@Component
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final OutboxProcessor outboxProcessor;


    @Scheduled(fixedDelayString = "PT5S")
    public void outboxEvent() {
        outboxProcessor.process();
    }
}
