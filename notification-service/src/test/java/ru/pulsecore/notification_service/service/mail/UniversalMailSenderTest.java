package ru.pulsecore.notification_service.service.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pulsecore.notification_service.properties.MailProperties;
import ru.pulsecore.notification_service.service.mail.sender.PdfMailSender;
import ru.pulsecore.notification_service.service.mail.sender.TextMailSender;
import ru.pulsecore.notification_service.service.mail.template.MailFormat;

import static org.mockito.Mockito.*;

class UniversalMailSenderTest {

    private TextMailSender textSender;
    private PdfMailSender pdfSender;
    private MailProperties props;
    private UniversalMailSender sender;

    @BeforeEach
    void setUp() {
        textSender = mock(TextMailSender.class);
        pdfSender = mock(PdfMailSender.class);
        props = mock(MailProperties.class);
        when(props.getFrom()).thenReturn("from@test.com");

        sender = new UniversalMailSender(textSender, pdfSender, props);
    }

    @Test
    void shouldSendTextEmail() {
        sender.send(MailFormat.TEXT, "to@test.com", "Subject", "Body", null, null);

        verify(textSender).send("from@test.com", "to@test.com", "Subject", "Body", null, null);
        verifyNoInteractions(pdfSender);
    }

    @Test
    void shouldSendPdfEmail() {
        byte[] pdf = {1, 2, 3};
        sender.send(MailFormat.PDF, "to@test.com", "Subject", null, "file.pdf", pdf);

        verify(pdfSender).send("from@test.com", "to@test.com", "Subject", null, "file.pdf", pdf);
        verifyNoInteractions(textSender);
    }
}