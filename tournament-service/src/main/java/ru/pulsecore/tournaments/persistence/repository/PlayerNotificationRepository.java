package ru.pulsecore.tournaments.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pulsecore.tournaments.persistence.entity.PlayerNotification;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerNotificationRepository
        extends JpaRepository<PlayerNotification, Long> {

    boolean existsByPlayerIdAndTournament_ExternalId(@Param("playerId") UUID playerId, @Param("externalId") Long externalId);

    void deleteByTournament_FinishedTrueAndTournament_DateBefore(@Param("date") java.time.LocalDate date);

    @Query("""
    SELECT pn
    FROM PlayerNotification pn
    JOIN FETCH pn.tournament t
    WHERE t.finished = false 
      AND t.cancelled = false 
      AND pn.pushReminderSent = false
    """)
    List<PlayerNotification> findPendingWithTournament();

    @Query("""
        SELECT pn
        FROM PlayerNotification pn
        JOIN FETCH pn.tournament t
        WHERE t.finished = false
    """)
    List<PlayerNotification> findNotFinishedFull();

    boolean existsByPlayerIdAndTournamentId(@Param("playerId") UUID playerId, @Param("tournamentId") Long tournamentId);

    void deleteByTournamentIdIn(@Param("ids") List<Long> tournamentIds);
}