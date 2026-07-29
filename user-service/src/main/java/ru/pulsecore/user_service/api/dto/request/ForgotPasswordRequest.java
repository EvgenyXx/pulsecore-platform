package ru.pulsecore.user_service.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record ForgotPasswordRequest(
        @NotBlank @Email @Schema(example = "evgenypavlov666@yandex.ru") String email
) {}