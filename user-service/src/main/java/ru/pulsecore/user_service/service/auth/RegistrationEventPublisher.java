package ru.pulsecore.user_service.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.VerificationContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;

@Component
@RequiredArgsConstructor
public class RegistrationEventPublisher {

    private static final int RECENT_DAYS = 30;

    private final KafkaEventPublisher publisher;

    public void verificationCode(String email, String code) {
        publisher.publish(
                KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(MailTypes.VERIFICATION, new VerificationContext(email, code))
        );
    }

    public void playerCreated(Player player, String ip, String userAgent) {
        publisher.publish(
                KafkaTopics.PLAYER_CREATED,
                new PlayerCreatedEvent(
                        player.getId(), player.getName(), player.getEmail(),
                        RECENT_DAYS, ip, userAgent
                )
        );
    }
}