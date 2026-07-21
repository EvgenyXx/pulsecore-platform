package ru.pulsecore.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.pulsecore.user_service.config.JwtCookieProperties;
import ru.pulsecore.user_service.config.JwtProperties;

@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication(scanBasePackages = "ru.pulsecore")
@EnableFeignClients
@EnableConfigurationProperties({JwtProperties.class,
		JwtCookieProperties.class})public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}