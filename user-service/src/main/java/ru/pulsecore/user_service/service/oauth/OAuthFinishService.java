package ru.pulsecore.user_service.service.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.user_service.api.dto.request.OAuthFinishRequest;
import ru.pulsecore.user_service.exception.auth.OAuthEmailNotReceivedException;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.service.auth.RegistrationEventPublisher;


@Service
@RequiredArgsConstructor
public class OAuthFinishService {

    private final OAuthSessionExtractor sessionExtractor;
    private final OAuthPlayerBuilder playerBuilder;
    private final TrialActivator trialActivator;
    private final RegistrationEventPublisher eventPublisher;

    @Transactional
    public void complete(OAuthFinishRequest request, HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        var data = sessionExtractor.extract(session);
        if (data.email() == null) {
            throw new OAuthEmailNotReceivedException();
        }

        String name = (request.getLastName() + " " + request.getFirstName()).toLowerCase().trim();
        String email = data.email();

        Player player = playerBuilder.build(name, email, data);
        trialActivator.activate(player);

        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        eventPublisher.playerCreated(player, ip, userAgent);

        sessionExtractor.clear(session);
    }
}