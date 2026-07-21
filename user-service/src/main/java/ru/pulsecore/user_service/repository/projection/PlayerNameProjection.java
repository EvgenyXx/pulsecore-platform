package ru.pulsecore.user_service.repository.projection;

import java.util.UUID;

public interface PlayerNameProjection {

    UUID getId();
    String getName();
    String getEmail();
}
