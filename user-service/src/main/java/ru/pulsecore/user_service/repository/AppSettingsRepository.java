package ru.pulsecore.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.pulsecore.user_service.domain.AppSettings;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppSettingsRepository extends JpaRepository<AppSettings, UUID> {


    Optional<AppSettings> findByKey(String key);
}