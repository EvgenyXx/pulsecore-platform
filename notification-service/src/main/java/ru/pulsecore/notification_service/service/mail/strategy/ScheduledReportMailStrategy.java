package ru.pulsecore.notification_service.service.mail.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pulsecore.notification_service.service.mail.MailStrategy;
import ru.pulsecore.notification_service.service.mail.MailTemplateService;

import ru.pulsecore.notification_service.service.mail.PdfReportGenerator;
import ru.pulsecore.notification_service.service.mail.UniversalMailSender;


import ru.pulsecore.notification_service.service.mail.template.MailFormat;
import ru.pulsecore.notification_service.service.mail.template.MailTemplate;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.MailContext;
import ru.pulsecore.shared.context.ScheduledReportContext;

@Component
@RequiredArgsConstructor
public class ScheduledReportMailStrategy implements MailStrategy {

    private final UniversalMailSender mailSender;
    private final PdfReportGenerator pdfGenerator;
    private final MailTemplateService templates;

    @Override
    public String getType() {
        return MailTypes.SCHEDULED_REPORT;
    }

    @Override
    public void send(MailContext ctx) {
        ScheduledReportContext c = (ScheduledReportContext) ctx;
        byte[] pdf = pdfGenerator.generate(c.sumResponse());
        String text = templates.format(MailTemplate.SCHEDULED_REPORT,
                c.period(), c.sum(), c.avg(), c.count());
        String fileName = "отчёт_" + c.period().replace(" – ", "_") + ".pdf";

        mailSender.send(
                MailFormat.PDF,
                c.to(),
                "PulseCore — Ваш отчёт за " + c.period(),
                text,
                fileName,
                pdf
        );
    }
}