package ru.pulsecore.user_service.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String id;
    private String name;
    private String email;
    private String accessToken;
}