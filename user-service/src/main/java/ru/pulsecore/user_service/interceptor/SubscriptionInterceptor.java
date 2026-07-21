package ru.pulsecore.user_service.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.pulsecore.user_service.exception.player.SubscriptionRequiredException;
import ru.pulsecore.user_service.service.subscription.SubscriptionService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubscriptionInterceptor implements HandlerInterceptor {

    private final SubscriptionService subscriptionService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof Jwt jwt)) {
            response.setStatus(401);
            return false;
        }

        UUID playerId = UUID.fromString(jwt.getSubject());
        if (!subscriptionService.hasActiveSubscription(playerId)) {
            throw new SubscriptionRequiredException();
        }
        return true;
    }
}