package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.api.dto.response.TournamentLiveDto;
import ru.pulsecore.tournaments.service.tournament.LiveService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Live")
@ApiV1TournamentController
@RequiredArgsConstructor
public class LiveController {

    private final LiveService liveService;


    @Operation(summary = "Мои залы для live-трансляций")
    @GetMapping(TournamentApi.LINEUPS_LIVE_HALLS)
    public ResponseEntity<String> getLiveHalls(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(liveService.getLiveSelectedHalls(UUID.fromString(jwt.getSubject())));
    }

    @Operation(summary = "Текущие live-трансляции")
    @GetMapping(TournamentApi.LIVE)
    public ResponseEntity<List<TournamentLiveDto>> getLive() {
        return ResponseEntity.ok(liveService.getLive());
    }

    @Operation(summary = "Количество онлайн по турнирам")
    @GetMapping(TournamentApi.ONLINE_ALL)
    public ResponseEntity<Map<Long, Long>> getAllOnline() {
        return ResponseEntity.ok(liveService.getOnlineCounts());
    }
}