package ru.pulsecore.notification_service.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategyRegistry;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.AdminNewUserContext;
import ru.pulsecore.shared.context.WelcomeContext;
import ru.pulsecore.shared.dto.event.PlayerCreatedEvent;
import ru.pulsecore.shared.properties.AdminProperties;
import ua_parser.Client;
import ua_parser.Parser;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayerCreatedListener {

    private final MailStrategyRegistry mailStrategyRegistry;
    private final AdminProperties adminProperties;
    private final Parser uaParser;

    @KafkaListener(topics = KafkaTopics.PLAYER_CREATED)
    public void handle(PlayerCreatedEvent event) {
        log.info("Player created: playerId={}", event.playerId());

        // Welcome
        if (event.email() != null && !event.email().isBlank()) {
            mailStrategyRegistry.send(MailTypes.WELCOME,
                    new WelcomeContext(event.email(), event.playerName()));
        }

        // Admin
        String agent = event.userAgent() != null ? event.userAgent() : "Неизвестно";
        Client client = uaParser.parse(agent);
        mailStrategyRegistry.send(MailTypes.ADMIN_NEW_USER,
                new AdminNewUserContext(
                        adminProperties.getEmail(),
                        event.playerName(),
                        event.email(),
                        event.ip(),
                        client.device.family,
                        client.os.family,
                        client.userAgent.family,
                        agent
                ));
    }
}