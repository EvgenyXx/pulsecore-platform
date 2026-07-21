package ru.pulsecore.shared.context;


import ru.pulsecore.shared.dto.tournament.response.SumResponse;

public record ScheduledReportContext(
        String to,
        String period,
        String sum,
        String avg,
        String count,
        SumResponse sumResponse
) implements MailContext {


}