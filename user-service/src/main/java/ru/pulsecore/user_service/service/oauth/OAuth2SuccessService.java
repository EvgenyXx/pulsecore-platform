package ru.pulsecore.user_service.service.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.service.CookieService;
import ru.pulsecore.user_service.service.jwt.AuthTokenService;


import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2SuccessService {

    private final OAuthDataExtractor dataExtractor;
    private final OAuthPlayerFinder playerFinder;
    private final OAuthSessionStorer sessionStorer;
    private final AuthTokenService authTokenService;
    private final CookieService cookieService;

    public void handle(OAuth2AuthenticationToken token,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String provider = token.getAuthorizedClientRegistrationId();
        var data = dataExtractor.extract(provider, token.getPrincipal());

        if (data == null) {
            response.sendRedirect("/");
            return;
        }

        Optional<Player> existing = playerFinder.find(provider, data.oauthId(), data.email());

        if (existing.isPresent()) {
            Player player = existing.get();
            var tokens = authTokenService.generateTokens(player);
            cookieService.setRefreshTokenCookie(response, tokens.refreshToken());
            response.sendRedirect("/dashboard?accessToken=" + tokens.accessToken());
        } else {
            sessionStorer.store(request, provider, data);
            response.sendRedirect("/oauth-finish.html");
        }
    }
}