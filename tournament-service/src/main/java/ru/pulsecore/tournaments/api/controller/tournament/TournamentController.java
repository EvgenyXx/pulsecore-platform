package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.MessageResponse;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.api.dto.request.UpdateResultRequest;
import ru.pulsecore.tournaments.service.tournament.TournamentFacade;

@Tag(name = "Турниры")
@ApiV1TournamentController
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentFacade tournamentFacade;

    @Operation(summary = "Обновить результат турнира")
    @PutMapping(TournamentApi.UPDATE_RESULT)
    public ResponseEntity<MessageResponse> updateResult(@RequestBody UpdateResultRequest updateResultRequest) {
        tournamentFacade.updateResult(
                updateResultRequest.id(),
                updateResultRequest.amount(),
                updateResultRequest.bonus());
        return ResponseEntity.ok(new MessageResponse("ok"));
    }
}