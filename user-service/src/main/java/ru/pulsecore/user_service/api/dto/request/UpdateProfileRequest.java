package ru.pulsecore.user_service.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.pulsecore.user_service.validation.RussianEmail;

@Data
public class UpdateProfileRequest {

    @NotBlank(message = "Email обязателен")
    @RussianEmail
    private String email;
}