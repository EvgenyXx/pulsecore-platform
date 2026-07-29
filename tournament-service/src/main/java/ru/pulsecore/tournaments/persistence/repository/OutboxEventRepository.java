package ru.pulsecore.tournaments.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pulsecore.tournaments.domain.OutBoxEvent;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutBoxEvent,Long> {

    List<OutBoxEvent> findBySentFalse();



}