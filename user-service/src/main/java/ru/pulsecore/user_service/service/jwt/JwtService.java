package ru.pulsecore.user_service.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.config.JwtKeyProvider;
import ru.pulsecore.user_service.config.JwtProperties;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.*;

@Service
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final Duration accessTtl;
    private final Duration refreshTtl;
    private final String issuer;

    public JwtService(JwtKeyProvider keyProvider, JwtProperties props) {
        this.privateKey = keyProvider.getPrivateKey();
        this.publicKey = keyProvider.getPublicKey();
        this.accessTtl = parse(props.getAccessTokenTtl());
        this.refreshTtl = parse(props.getRefreshTokenTtl());
        this.issuer = props.getIssuer();
    }

    public String generateAccessToken(UUID userId, String name, String email, List<String> roles) {
        return Jwts.builder()
                .subject(userId.toString())
                .claims(Map.of("name", name, "email", email, "roles", roles))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTtl.toMillis()))
                .issuer(issuer)
                .signWith(privateKey)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("jti", UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTtl.toMillis()))
                .issuer(issuer)
                .signWith(privateKey)
                .compact();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Duration parse(String ttl) {
        ttl = ttl.toLowerCase();
        if (ttl.endsWith("d")) return Duration.ofDays(Long.parseLong(ttl.replace("d", "")));
        if (ttl.endsWith("h")) return Duration.ofHours(Long.parseLong(ttl.replace("h", "")));
        if (ttl.endsWith("m")) return Duration.ofMinutes(Long.parseLong(ttl.replace("m", "")));
        return Duration.ofSeconds(Long.parseLong(ttl));
    }
}