package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.api.dto.response.LineupDto;
import ru.pulsecore.tournaments.service.lineup.LineupFacade;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Составы")
@ApiV1TournamentController
@RequiredArgsConstructor
public class LineupController {

    private final LineupFacade lineupFacade;

    @Operation(summary = "Все составы на дату")
    @GetMapping(TournamentApi.LINEUPS_ALL)
    public ResponseEntity<Map<String, List<LineupDto>>> getAll(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(lineupFacade.getAllGroupedByHall(date));
    }

    @Operation(summary = "Мои составы на дату")
    @GetMapping(TournamentApi.LINEUPS_MY)
    public ResponseEntity<Map<String, List<LineupDto>>> getMy(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(lineupFacade.getMyGroupedByHall(UUID.fromString(jwt.getSubject()), date));
    }


}