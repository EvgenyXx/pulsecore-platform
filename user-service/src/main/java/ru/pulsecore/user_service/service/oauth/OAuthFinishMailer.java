package ru.pulsecore.user_service.service.oauth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.AdminNewUserContext;
import ru.pulsecore.shared.context.WelcomeContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.properties.AdminProperties;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthFinishMailer {

    private final KafkaEventPublisher publisher;
    private final AdminProperties adminProperties;
    private final Parser uaParser;

    public void sendWelcome(Player player) {
        if (player.getEmail() != null && player.getEmail().contains("@")) {
            publisher.publish(KafkaTopics.EMAIL_NOTIFICATION,new MailNotificationEvent(MailTypes.WELCOME,
                    new WelcomeContext(player.getEmail(), player.getName())));
        }
    }

    public void notifyAdmin(Player player, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent") != null ?
                request.getHeader("User-Agent") : "Неизвестно";
        Client client = uaParser.parse(userAgent);

        publisher.publish(KafkaTopics.EMAIL_NOTIFICATION,new MailNotificationEvent(MailTypes.ADMIN_NEW_USER,
                new AdminNewUserContext(
                        adminProperties.getEmail(),
                        player.getName(),
                        player.getEmail() != null ? player.getEmail() : "нет",
                        ip,
                        client.device.family,
                        client.os.family,
                        client.userAgent.family,
                        userAgent)));
    }
}