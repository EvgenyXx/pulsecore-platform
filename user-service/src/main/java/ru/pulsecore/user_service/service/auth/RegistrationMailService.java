package ru.pulsecore.user_service.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.AdminNewUserContext;
import ru.pulsecore.shared.context.VerificationContext;
import ru.pulsecore.shared.context.WelcomeContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.properties.AdminProperties;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;
import ua_parser.Client;
import ua_parser.Parser;

@Component
@RequiredArgsConstructor
public class RegistrationMailService {

    private final AdminProperties adminProperties;//todo что то думать  с этим
    private final Parser uaParser;
    private final KafkaEventPublisher kafkaEventPublisher;

    public void sendVerificationCode(String email, String code) {
        kafkaEventPublisher.publish(
                KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(
                MailTypes.VERIFICATION,new VerificationContext(email, code)));
    }

    public void sendWelcome(Player player) {
        if (player.getEmail() != null && player.getEmail().contains("@")) {
            kafkaEventPublisher.publish(
                    KafkaTopics.EMAIL_NOTIFICATION,
                    new MailNotificationEvent(
                    MailTypes.WELCOME,new WelcomeContext(player.getEmail(), player.getName())));
        }
    }

    public void notifyAdminNewUser(Player player, String ip, String userAgent) {
        String agent = userAgent != null ? userAgent : "Неизвестно";
        Client client = uaParser.parse(agent);
        kafkaEventPublisher.publish(
                KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(
                MailTypes.ADMIN_NEW_USER,
                new AdminNewUserContext(adminProperties.getEmail(),
                        player.getName(), player.getEmail(), ip,
                        client.device.family, client.os.family, client.userAgent.family, agent)));
    }
}