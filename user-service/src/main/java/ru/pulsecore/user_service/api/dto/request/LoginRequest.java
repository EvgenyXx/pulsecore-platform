package ru.pulsecore.user_service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record LoginRequest(
        @NotBlank @Email @Schema(example = "evgenypavlov666@yandex.ru") String email,
        @NotBlank @Schema(example = "123456") String password
) {}