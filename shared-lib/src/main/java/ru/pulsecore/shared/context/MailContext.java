package ru.pulsecore.shared.context;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WelcomeContext.class, name = "welcome"),
        @JsonSubTypes.Type(value = VerificationContext.class, name = "verification"),
        @JsonSubTypes.Type(value = PasswordResetContext.class, name = "password_reset"),
        @JsonSubTypes.Type(value = BroadcastContext.class, name = "broadcast"),
        @JsonSubTypes.Type(value = NewTournamentContext.class, name = "new_tournament"),
        @JsonSubTypes.Type(value = TournamentResultContext.class, name = "tournament_result"),
        @JsonSubTypes.Type(value = ScheduledReportContext.class, name = "scheduled_report"),
        @JsonSubTypes.Type(value = AdminNewUserContext.class, name = "admin_new_user"),
        @JsonSubTypes.Type(value = AdminPaymentContext.class, name = "admin_payment")
})
public interface MailContext {

}