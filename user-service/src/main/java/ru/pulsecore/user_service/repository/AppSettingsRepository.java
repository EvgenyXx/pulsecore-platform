package ru.pulsecore.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pulsecore.user_service.domain.AppSettings;


import java.util.Optional;
import java.util.UUID;

public interface AppSettingsRepository extends JpaRepository<AppSettings, UUID> {
    Optional<AppSettings> findByKey(String key);
}