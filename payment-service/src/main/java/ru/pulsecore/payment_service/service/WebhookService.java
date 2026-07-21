package ru.pulsecore.payment_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.payment_service.event.KafkaEventPublisher;
import ru.pulsecore.payment_service.api.dto.internal.WebhookObject;
import ru.pulsecore.payment_service.api.dto.reqauest.WebhookRequest;
import ru.pulsecore.payment_service.domain.Payment;
import ru.pulsecore.payment_service.repository.PaymentRepository;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.AdminPaymentContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.shared.dto.event.payment.PaymentSucceededEvent;
import ru.pulsecore.shared.properties.AdminProperties;


import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final KafkaEventPublisher kafkaEventPublisher;
    private final AdminProperties adminProperties;
    private final PaymentRepository paymentRepository;

    private static final String EVENT_SUCCEEDED = "payment.succeeded";
    private static final String METADATA_PLAYER_ID = "playerId";
    private static final String METADATA_MONTHS = "months";
    private static final int DAYS_PER_MONTH = 30;

    public void process(WebhookRequest request) {
        if (!EVENT_SUCCEEDED.equals(request.event())) {
            log.debug("Ignored: event is not succeeded");
            return;
        }

        WebhookObject payment = request.object();
        if (payment.metadata() == null || !payment.metadata().containsKey(METADATA_PLAYER_ID)) {
            log.debug("Ignored: no playerId in metadata");
            return;
        }

        UUID playerId = UUID.fromString(payment.metadata().get(METADATA_PLAYER_ID));
        int months = Integer.parseInt(payment.metadata().get(METADATA_MONTHS));
        int days = months * DAYS_PER_MONTH;

        kafkaEventPublisher.publish(KafkaTopics.PAYMENT_EVENTS,
                new PaymentSucceededEvent(playerId, days));

        paymentRepository.save(Payment.builder()
                .playerId(playerId)
                .amount(Integer.parseInt(payment.amount().value()))
                .months(months)
                .paymentId(payment.id())
                .build());

        kafkaEventPublisher.publish(KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(MailTypes.ADMIN_PAYMENT_RECEIVED,
                        new AdminPaymentContext(adminProperties.getEmail(), playerId.toString(),
                                months, payment.amount().value(), payment.amount().currency())));

        log.info("Платёж обработан: playerId={}, months={}", playerId, months);
    }
}