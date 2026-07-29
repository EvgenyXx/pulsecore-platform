package ru.pulsecore.notification_service.config;

import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class PushConfig {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Bean
    public PushService pushService(VapidConfig vapidConfig) throws Exception {
        return new PushService()
                .setPublicKey(vapidConfig.getPublicKey())
                .setPrivateKey(vapidConfig.getPrivateKey())
                .setSubject("mailto:noreply@pulsecore-app.ru");
    }
}