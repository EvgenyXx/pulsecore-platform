package ru.pulsecore.user_service.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.config.constants.MailTypes;
import ru.pulsecore.shared.context.PasswordResetContext;
import ru.pulsecore.shared.dto.event.MailNotificationEvent;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.exception.player.BadResetCodeException;
import ru.pulsecore.user_service.repository.PlayerRepository;
import ru.pulsecore.user_service.event.publisher.KafkaEventPublisher;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PlayerPasswordResetService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String PREFIX = "reset:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String,String> redisTemplate;
    private final KafkaEventPublisher playerEventPublisher;

    public void initiate(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        String code = String.format("%06d", RANDOM.nextInt(999999));
        redisTemplate.opsForValue().set(PREFIX + normalizedEmail, code, TTL);
        playerEventPublisher.publish(
                KafkaTopics.EMAIL_NOTIFICATION,
                new MailNotificationEvent(
                        MailTypes.PASSWORD_RESET,
                        new PasswordResetContext(normalizedEmail, code)
                )
        );

    }

    @Transactional
    public void complete(String email, String code, String newPassword) {
        String normalizedEmail = email.toLowerCase().trim();
        String expectedCode = redisTemplate.opsForValue().get(PREFIX + normalizedEmail);
        if (expectedCode == null || !expectedCode.equals(code)) {
            throw new BadResetCodeException();
        }
        Player player = playerRepository.findByEmail(normalizedEmail)
                .orElseThrow(BadResetCodeException::new);
        player.setPassword(passwordEncoder.encode(newPassword));
        playerRepository.save(player);
        redisTemplate.delete(PREFIX + normalizedEmail);
    }
}