package ru.pulsecore.notification_service.service.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.pulsecore.notification_service.service.mail.template.MailTemplate;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailTemplateServiceTest {

    private ResourceLoader resourceLoader;
    private MailTemplateService mailTemplateService;

    @BeforeEach
    void setUp() {
        resourceLoader = mock(ResourceLoader.class);
        mailTemplateService = new MailTemplateService(resourceLoader);
    }

    @Test
    void shouldFormatTemplate() throws Exception {
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getContentAsString(StandardCharsets.UTF_8)).thenReturn("Hello %s!");

        // Используем реальный enum или замоканный
        MailTemplate template = mock(MailTemplate.class);
        when(template.getFileName()).thenReturn("test");

        String result = mailTemplateService.format(template, "User");

        assertEquals("Hello User!", result);
    }

    @Test
    void shouldCacheTemplate() throws Exception {
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getContentAsString(StandardCharsets.UTF_8)).thenReturn("Cached");

        MailTemplate template = mock(MailTemplate.class);
        when(template.getFileName()).thenReturn("test");

        mailTemplateService.getTemplate(template);
        mailTemplateService.getTemplate(template);

        verify(resourceLoader, times(1)).getResource(anyString());
    }
}