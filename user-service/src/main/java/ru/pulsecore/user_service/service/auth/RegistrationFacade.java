package ru.pulsecore.user_service.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.pulsecore.user_service.api.dto.response.AuthResponse;
import ru.pulsecore.user_service.domain.Player;
import ru.pulsecore.user_service.exception.player.BadCredentialsException;

import ru.pulsecore.user_service.service.jwt.AuthTokenService;
import ru.pulsecore.user_service.service.role.RoleService;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RegistrationFacade {

    private static final String PREFIX = "reg:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final RegistrationValidator validator;
    private final VerificationCodeGenerator codeGenerator;
    private final RegistrationMailService mailService;
    private final PlayerFactory playerFactory;
    private final RoleService roleService;
    private final AuthTokenService authTokenService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostRegistrationService postRegistrationService;

    public void initiate(String name, String email, String rawPassword) {
        validator.validate(email, name);
        String code = codeGenerator.generate();
        mailService.sendVerificationCode(email, code);

        Pending pending = new Pending(name, email, rawPassword, code);
        redisTemplate.opsForValue().set(PREFIX + email, pending, TTL);

    }

    @Transactional
    public RegistrationResult complete(String email, String code, String ip, String userAgent) {
        Pending pending = (Pending) redisTemplate.opsForValue().get(PREFIX + email);
        if (pending == null || !pending.code().equals(code)) {
            throw new BadCredentialsException();
        }

        var defaultRole = roleService.findRoleUser();
        Player player = playerFactory.create(pending.name(), pending.email(), pending.password(), defaultRole);

        mailService.notifyAdminNewUser(player,ip,userAgent);
        mailService.sendWelcome(player);
        postRegistrationService.execute(player);

        var tokens = authTokenService.generateTokens(player);
        redisTemplate.delete(PREFIX + email);

        AuthResponse response = AuthResponse.builder()
                .id(player.getId().toString())
                .name(player.getName())
                .email(player.getEmail())
                .accessToken(tokens.accessToken())
                .build();

        return new RegistrationResult(response, tokens.refreshToken());
    }

    public record Pending(String name, String email, String password, String code) implements java.io.Serializable {}

    public record RegistrationResult(AuthResponse response, String refreshToken) {}
}