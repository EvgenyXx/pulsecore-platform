package ru.pulsecore.shared.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pulsecore.shared.context.MailContext;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailNotificationEvent {
    private String type;
    private MailContext context;
}