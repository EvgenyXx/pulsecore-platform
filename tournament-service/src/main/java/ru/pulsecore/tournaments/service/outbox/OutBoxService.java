package ru.pulsecore.tournaments.service.outbox;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.util.JsonUtils;
import ru.pulsecore.tournaments.domain.OutBoxEvent;

import ru.pulsecore.tournaments.persistence.repository.OutboxEventRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OutBoxService {

    private final OutboxEventRepository outboxEventRepository;


    @Transactional(propagation = Propagation.MANDATORY)
    public void save(String topic, Object object) {

        try {
            String payload = JsonUtils.toJson(object);
            OutBoxEvent event = OutBoxEvent.builder()
                    .topic(topic)
                    .payload(payload)
                    .build();
            outboxEventRepository.save(event);
        } catch (Exception e) {
            log.error("Error creating outbox event", e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveAll(String topic, List<Object> objects) {
       try {
           List<OutBoxEvent> events = new ArrayList<>();
           for (var object : objects) {
               String payload = JsonUtils.toJson(object);
               OutBoxEvent event = OutBoxEvent.builder()
                       .topic(topic)
                       .payload(payload)
                       .build();
               events.add(event);
           }
           outboxEventRepository.saveAll(events);
       }catch (Exception e) {
           log.error("Error creating outbox event", e);
       }
    }
}