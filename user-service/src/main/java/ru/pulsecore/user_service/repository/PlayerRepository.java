package ru.pulsecore.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pulsecore.user_service.domain.Player;

import ru.pulsecore.user_service.repository.projection.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query("SELECT p.name FROM Player p WHERE p.id = :playerId")
    String findNameById(@Param("playerId") UUID playerId);


    @Query("SELECT p.id AS id, p.name AS name, p.email AS email FROM Player p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PlayerNameProjection> searchByName(@Param("query") String query);

    @Query(value = "SELECT CAST(p.id AS text) AS id, CAST(p.name AS text) AS name," +
            " CAST(p.email AS text) AS email FROM players p WHERE p.id = :id", nativeQuery = true)
    Optional<PlayerDataProjection> findDataById(@Param("id") UUID id);

    @Query(value = "SELECT p.push_enabled AS pushEnabled, p.notifications_enabled AS notificationsEnabled, " +
            "COALESCE(s.active, false) AS activeSubscription " +
            "FROM players p LEFT JOIN subscription s ON s.player_id = p.id WHERE p.id = :id", nativeQuery = true)
    Optional<PlayerSettingsProjection> findSettingsById(@Param("id") UUID id);

    @Query("SELECT p.id FROM Player p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<UUID> findIdByNameIgnoreCase(@Param("name") String name);


    boolean existsByEmail(String email);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT p FROM Player p WHERE LOWER(p.email) = LOWER(:email)")
    Optional<Player> findByEmail(@Param("email") String email);

    @Query("SELECT p.id AS id, p.name AS name, p.email AS email FROM Player p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PlayerSearchProjection> searchByNameOrEmail(@Param("query") String query);

    List<Player> findByVerifiedFalseAndCreatedAtBefore(LocalDateTime cutoff);


    Optional<Player> findByOauthProviderAndOauthId(String provider, String oauthId);

    @Query("SELECT p.id AS id, p.email AS email, p.pushEnabled AS pushEnabled" +
            " FROM Player p WHERE p.verified = true AND p.isBlocked = false")
    List<BroadcastProjection> findBroadcastRecipients();



}