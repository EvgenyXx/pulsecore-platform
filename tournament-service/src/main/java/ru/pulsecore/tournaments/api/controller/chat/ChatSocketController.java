package ru.pulsecore.tournaments.api.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import ru.pulsecore.tournaments.api.ChatSocketApi;
import ru.pulsecore.tournaments.api.TournamentApi;
import ru.pulsecore.tournaments.api.dto.response.ChatMessageDto;
import ru.pulsecore.tournaments.service.tournament.ChatFacade;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatFacade chatFacade;

    @MessageMapping(ChatSocketApi.SEND)
    public void sendMessage(@DestinationVariable(TournamentApi.PARAM_LINEUP_ID) Long lineupId, ChatMessageDto msg) {
        chatFacade.sendMessage(lineupId, msg);
    }
}