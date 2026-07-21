package ru.pulsecore.user_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pulsecore.user_service.domain.ReportStatus;
import ru.pulsecore.user_service.domain.ScheduledReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledReportRepository extends JpaRepository<ScheduledReport, UUID> {
    List<ScheduledReport> findByStatusAndScheduledAtBefore(ReportStatus reportStatus, LocalDateTime time);

    List<ScheduledReport> findByPlayerIdAndStatus(UUID playerId, ReportStatus status);
}
