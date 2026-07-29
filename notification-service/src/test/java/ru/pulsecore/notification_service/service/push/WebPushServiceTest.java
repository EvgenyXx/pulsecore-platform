package ru.pulsecore.notification_service.service.push;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.martijndwars.webpush.PushService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pulsecore.notification_service.domain.PushSubscription;
import ru.pulsecore.notification_service.repository.PushSubscriptionRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class WebPushServiceTest {

    private PushSubscriptionRepository repository;
    private PushService pushService;
    private WebPushService webPushService;

    @BeforeEach
    void setUp() {
        repository = mock(PushSubscriptionRepository.class);
        pushService = mock(PushService.class);
        webPushService = new WebPushService(repository, pushService, new ObjectMapper());
    }

    @Test
    void shouldNotSendWhenNoSubscriptions() {
        UUID playerId = UUID.randomUUID();
        when(repository.findByPlayerId(playerId)).thenReturn(List.of());

        webPushService.sendToPlayer(playerId, "title", "body", "url");

        verify(repository).findByPlayerId(playerId);
        verifyNoInteractions(pushService);
    }

}