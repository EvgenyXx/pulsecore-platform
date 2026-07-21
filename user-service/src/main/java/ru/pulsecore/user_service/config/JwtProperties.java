package ru.pulsecore.user_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private Resource privateKey;
    private Resource publicKey;
    private String accessTokenTtl;
    private String refreshTokenTtl;
    private String issuer;
}