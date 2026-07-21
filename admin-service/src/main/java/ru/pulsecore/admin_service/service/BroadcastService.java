package ru.pulsecore.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.admin_service.client.PlayerClient;
import ru.pulsecore.admin_service.event.KafkaEventPublisher;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.context.BroadcastContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.dto.player.BroadcastRecipient;

import ru.pulsecore.shared.config.constants.MailTypes;


import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BroadcastService {


    private static final String PUSH_TITLE = "PulseCore";
    private static final String PUSH_URL = "/dashboard";


    private final PlayerClient dataClient;
    private final KafkaEventPublisher kafkaEventPublisher;

    public BroadcastResult broadcast(String message) {
        List<BroadcastRecipient> recipients = dataClient.getBroadcastRecipients();

        int pushSent = 0;
        int emailSent = 0;

        for (BroadcastRecipient recipient : recipients) {
            if (sendPush(recipient, message)) pushSent++;
            if (sendEmail(recipient, message)) emailSent++;
        }

        log.info("Рассылка завершена. Push: {}, Email: {}", pushSent, emailSent);

        return new BroadcastResult(recipients.size(), pushSent, emailSent);
    }

    private boolean sendPush(BroadcastRecipient recipient, String message) {
        if (!recipient.pushEnabled()) return false;
        try {
            kafkaEventPublisher.publish(
                    KafkaTopics.PUSH_NOTIFICATION,
                    new PushNotificationEvent(recipient.id(), PUSH_TITLE, message, PUSH_URL));
            return true;
        } catch (Exception e) {
            log.error("Push не отправлен playerId={}: {}", recipient.id(), e.getMessage());
            return false;
        }
    }

    private boolean sendEmail(BroadcastRecipient recipient, String message) {
        if (recipient.email() == null || recipient.email().isBlank()) return false;
        try {
            kafkaEventPublisher.publish(
                    KafkaTopics.EMAIL_NOTIFICATION,
                   new  MailNotificationEvent(
                    MailTypes.BROADCAST,
                    new BroadcastContext(recipient.email(), message)));
            return true;
        } catch (Exception e) {
            log.error("Email не отправлен playerId={}: {}", recipient.id(), e.getMessage());
            return false;
        }
    }
}