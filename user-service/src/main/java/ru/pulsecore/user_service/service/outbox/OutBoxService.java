package ru.pulsecore.user_service.service.outbox;



import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.util.JsonUtils;
import ru.pulsecore.user_service.domain.OutboxEvent;
import ru.pulsecore.user_service.repository.OutboxEventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutBoxService {

    private final OutboxEventRepository outboxEventRepository;


    @Transactional(propagation = Propagation.MANDATORY)
    public void save(String topic, Object object) {

        try {
            String payload = JsonUtils.toJson(object);
            OutboxEvent event = OutboxEvent.builder()
                    .topic(topic)
                    .payload(payload)
                    .build();
            outboxEventRepository.save(event);
        }catch (Exception e) {
            log.error("Error creating outbox event", e);
        }

    }
}
