package ru.pulsecore.tournaments.config;

import feign.FeignException;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class PlayerServiceFeignConfig {

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> switch (response.status()) {
            case 404 -> new NotFoundException("Пользователь не найден");
            case 503 -> new ServiceUnavailableException("Сервис недоступен");
            default -> FeignException.errorStatus(methodKey, response);
        };
    }
    @Bean
    public Request.Options options() {
        return new Request.Options(
                5, 
                TimeUnit.SECONDS, 
                10, TimeUnit.SECONDS,
                true);
    }
}