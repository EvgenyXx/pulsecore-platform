package ru.pulsecore.notification_service.service.push;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pulsecore.notification_service.api.dto.PushSubscriptionRequest;
import ru.pulsecore.notification_service.config.VapidConfig;
import ru.pulsecore.notification_service.domain.PushSubscription;
import ru.pulsecore.notification_service.repository.PushSubscriptionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PushFacadeTest {

    private PushSubscriptionRepository repository;
    private VapidConfig vapidConfig;
    private PushFacade pushFacade;

    @BeforeEach
    void setUp() {
        repository = mock(PushSubscriptionRepository.class);
        vapidConfig = mock(VapidConfig.class);
        pushFacade = new PushFacade(repository, vapidConfig);
    }

    @Test
    void shouldReturnTrueWhenSubscribed() {
        UUID playerId = UUID.randomUUID();
        when(repository.findByPlayerId(playerId)).thenReturn(List.of(new PushSubscription()));

        assertTrue(pushFacade.isSubscribed(playerId));
    }

    @Test
    void shouldReturnFalseWhenNotSubscribed() {
        UUID playerId = UUID.randomUUID();
        when(repository.findByPlayerId(playerId)).thenReturn(List.of());

        assertFalse(pushFacade.isSubscribed(playerId));
    }

    @Test
    void shouldReturnVapidPublicKey() {
        when(vapidConfig.getPublicKey()).thenReturn("test-public-key");

        assertEquals("test-public-key", pushFacade.getVapidPublicKey());
    }

    @Test
    void shouldSubscribeWhenNotAlreadySubscribed() {
        UUID playerId = UUID.randomUUID();
        PushSubscriptionRequest request = new PushSubscriptionRequest("endpoint", "p256dh", "auth");
        when(repository.findByPlayerIdAndEndpoint(playerId, "endpoint")).thenReturn(Optional.empty());

        pushFacade.subscribe(playerId, request);

        verify(repository).save(any(PushSubscription.class));
    }

    @Test
    void shouldNotDuplicateSubscription() {
        UUID playerId = UUID.randomUUID();
        PushSubscriptionRequest request = new PushSubscriptionRequest("endpoint", "p256dh", "auth");
        when(repository.findByPlayerIdAndEndpoint(playerId, "endpoint"))
                .thenReturn(Optional.of(new PushSubscription()));

        pushFacade.subscribe(playerId, request);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldUnsubscribeWhenExists() {
        UUID playerId = UUID.randomUUID();
        PushSubscription sub = new PushSubscription();
        when(repository.findByPlayerIdAndEndpoint(playerId, "endpoint")).thenReturn(Optional.of(sub));

        pushFacade.unsubscribe(playerId, "endpoint");

        verify(repository).delete(sub);
    }

    @Test
    void shouldDoNothingWhenUnsubscribeNotFound() {
        UUID playerId = UUID.randomUUID();
        when(repository.findByPlayerIdAndEndpoint(playerId, "endpoint")).thenReturn(Optional.empty());

        pushFacade.unsubscribe(playerId, "endpoint");

        verify(repository, never()).delete(any());
    }
}