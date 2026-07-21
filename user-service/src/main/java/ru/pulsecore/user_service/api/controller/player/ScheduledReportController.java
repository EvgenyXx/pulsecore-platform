
package ru.pulsecore.user_service.api.controller.player;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.user_service.api.annotation.ApiV1PlayerController;

import ru.pulsecore.user_service.api.PlayerApi;

import ru.pulsecore.user_service.api.dto.response.ScheduledReportResponse;
import ru.pulsecore.user_service.api.dto.request.CreateScheduledReportRequest;
import ru.pulsecore.user_service.scheduler.ScheduledReportFacade;


import java.util.List;
import java.util.UUID;

@Tag(name = "Заказные отчёты")
@ApiV1PlayerController
@RequiredArgsConstructor
public class ScheduledReportController {

    private final ScheduledReportFacade facade;

    @Operation(summary = "Создать заказной отчёт на почту")
    @PostMapping(PlayerApi.REPORTS)
    public ResponseEntity<MessageResponse> createReport(
            @Valid @RequestBody CreateScheduledReportRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        facade.createReport(UUID.fromString(jwt.getSubject()),
                request.dateFrom(), request.dateTo(), request.scheduledAt());
        return ResponseEntity.ok(new MessageResponse("ok"));
    }

    @Operation(summary = "Получить список ожидающих отчётов")
    @GetMapping(PlayerApi.REPORTS_PENDING)
    public ResponseEntity<List<ScheduledReportResponse>> getReports(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(facade.getPlayerReports(UUID.fromString(jwt.getSubject())));
    }

    @Operation(summary = "Отменить заказной отчёт")
    @DeleteMapping(PlayerApi.REPORTS_CANCEL)
    public ResponseEntity<Void> cancelReport(@PathVariable UUID id) {
        facade.deleteByScheduled(id);
        return ResponseEntity.noContent().build();
    }
}