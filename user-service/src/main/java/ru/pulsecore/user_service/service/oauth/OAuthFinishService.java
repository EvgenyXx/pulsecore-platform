package ru.pulsecore.user_service.service.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.user_service.api.dto.request.OAuthFinishRequest;
import ru.pulsecore.user_service.exception.auth.OAuthEmailNotReceivedException;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;


@Service
@RequiredArgsConstructor
public class OAuthFinishService {

    private final OAuthSessionExtractor sessionExtractor;
    private final OAuthPlayerBuilder playerBuilder;
    private final TrialActivator trialActivator;
    private final OAuthFinishMailer mailer;
    private final KafkaEventPublisher publisher;

    @Transactional
    public void complete(OAuthFinishRequest request, HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        var data = sessionExtractor.extract(session);
        if (data.email() == null) {
            throw new OAuthEmailNotReceivedException();
        }

        String name = (request.getLastName() + " " + request.getFirstName()).toLowerCase().trim();
        String email = data.email();

        Player player = playerBuilder.build(name, email, data);
        trialActivator.activate(player);

        publisher.publish(
                KafkaTopics.PLAYER_CREATED,
                new PlayerCreatedEvent(
                player.getId(),
                player.getName(),
                player.getEmail(),
                30));

        mailer.sendWelcome(player);
        mailer.notifyAdmin(player, httpRequest);
        sessionExtractor.clear(session);

    }
}