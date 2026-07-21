package ru.pulsecore.tournaments.service.tournament;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;


import ru.pulsecore.tournaments.api.dto.response.ChatMessageDto;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.event.KafkaPublisher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMentionService {

    private static final Pattern MENTION_PATTERN = Pattern.compile("@([\\p{L}]+)\\s+([\\p{L}]+)");



    private final KafkaPublisher kafkaPublisher;
    private final PlayerClient searchByName;

    public List<PlayerSearchResponse> searchPlayers(String query) {
        if (query == null || query.isBlank()) return List.of();
        return searchByName.searchByName(query);
    }

    public void processMentions(Long lineupId, ChatMessageDto msg) {
        if (msg.getMessage() == null) return;
        Set<UUID> mentionedIds = new HashSet<>();
        Matcher matcher = MENTION_PATTERN.matcher(msg.getMessage());

        while (matcher.find()) {
            String fullName = matcher.group(1) + " " + matcher.group(2);
           UUID playerId =  searchByName.getIdByFullName(fullName);
                if (!playerId.equals(msg.getPlayerId())) {
                    mentionedIds.add(playerId);
                }

        }

        for (UUID playerId : mentionedIds) {
                kafkaPublisher.publish(KafkaTopics.PUSH_NOTIFICATION,new PushNotificationEvent(
                        playerId,
                        "💬 " + msg.getPlayerName(),
                        msg.getMessage(),
                        "/live/" + lineupId
                ));
        }
    }
}