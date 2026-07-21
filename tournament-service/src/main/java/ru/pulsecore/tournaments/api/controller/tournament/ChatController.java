package ru.pulsecore.tournaments.api.controller.tournament;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.annotation.ApiV1TournamentController;
import ru.pulsecore.tournaments.api.dto.response.ChatMessageDto;
import ru.pulsecore.tournaments.service.tournament.ChatFacade;

import java.util.List;
import java.util.UUID;

@Tag(name = "Чат")
@ApiV1TournamentController
@RequiredArgsConstructor
public class ChatController {

    private final ChatFacade chatFacade;

    @Operation(summary = "Получить сообщения чата")
    @GetMapping(TournamentApi.CHAT_LINEUP)
    public ResponseEntity<List<ChatMessageDto>> getMessages(
            @PathVariable(TournamentApi.PARAM_LINEUP_ID) Long lineupId,
            @RequestParam(required = false) Long after) {
        return ResponseEntity.ok(chatFacade.getMessages(lineupId, after));
    }

    @Operation(summary = "Отправить сообщение в чат")
    @PostMapping(TournamentApi.CHAT_LINEUP)
    public ResponseEntity<ChatMessageDto> send(
            @PathVariable(TournamentApi.PARAM_LINEUP_ID) Long lineupId,
            @RequestBody ChatMessageDto msg) {
        return ResponseEntity.ok(chatFacade.sendMessage(lineupId, msg));
    }

    @Operation(summary = "Поиск игрока через @  что бы ему можно было написать ")
    @GetMapping(TournamentApi.CHAT_PLAYERS_SEARCH)
    public ResponseEntity<List<PlayerSearchResponse>> searchPlayers(@RequestParam String q) {
        return ResponseEntity.ok(chatFacade.searchPlayers(q));
    }

    @Operation(summary = "Удалить сообщение")
    @DeleteMapping(TournamentApi.CHAT_MESSAGE)
    public ResponseEntity<Void> deleteMessage(
            @PathVariable(TournamentApi.PARAM_MESSAGE_ID) Long messageId,
            @AuthenticationPrincipal Jwt jwt) {
        chatFacade.deleteMessage(messageId, UUID.fromString(jwt.getSubject()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Редактировать сообщение")
    @PatchMapping(TournamentApi.CHAT_MESSAGE)
    public ResponseEntity<Void> updateMessage(
            @PathVariable(TournamentApi.PARAM_MESSAGE_ID) Long messageId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ChatMessageDto msg) {
        chatFacade.updateMessage(messageId, UUID.fromString(jwt.getSubject()), msg.getMessage());
        return ResponseEntity.ok().build();
    }
}