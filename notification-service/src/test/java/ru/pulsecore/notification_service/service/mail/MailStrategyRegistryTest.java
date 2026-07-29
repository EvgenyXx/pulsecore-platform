package ru.pulsecore.notification_service.service.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pulsecore.notification_service.exception.MailStrategyNotFoundException;
import ru.pulsecore.shared.context.MailContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailStrategyRegistryTest {

    private MailStrategy welcomeStrategy;
    private MailStrategy adminStrategy;
    private MailStrategyRegistry registry;

    @BeforeEach
    void setUp() {
        welcomeStrategy = mock(MailStrategy.class);
        when(welcomeStrategy.getType()).thenReturn("welcome");

        adminStrategy = mock(MailStrategy.class);
        when(adminStrategy.getType()).thenReturn("admin");

        registry = new MailStrategyRegistry(List.of(welcomeStrategy, adminStrategy));
    }

    @Test
    void shouldSendUsingCorrectStrategy() {
        MailContext ctx = mock(MailContext.class);

        registry.send("welcome", ctx);

        verify(welcomeStrategy).send(ctx);
        verify(adminStrategy, never()).send(any());
    }

    @Test
    void shouldThrowWhenStrategyNotFound() {
        MailContext ctx = mock(MailContext.class);

        assertThrows(MailStrategyNotFoundException.class,
                () -> registry.send("unknown", ctx));
    }
}