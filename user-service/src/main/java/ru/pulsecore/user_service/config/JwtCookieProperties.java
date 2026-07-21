package ru.pulsecore.user_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt.cookie")
public class JwtCookieProperties {
    private String name = "refresh_token";
    private String path = "/";
    private boolean secure = false;
    private boolean httpOnly = true;
    private String sameSite = "Lax";
    private Duration maxAge = Duration.ofDays(30);
    private String domain;
}