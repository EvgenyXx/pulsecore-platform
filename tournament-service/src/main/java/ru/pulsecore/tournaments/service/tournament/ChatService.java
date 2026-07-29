package ru.pulsecore.tournaments.service.tournament;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pulsecore.shared.config.constants.KafkaTopics;
import ru.pulsecore.shared.dto.event.PushNotificationEvent;
import ru.pulsecore.shared.dto.player.PlayerSearchResponse;
import ru.pulsecore.shared.exception.ForbiddenException;
import ru.pulsecore.tournaments.client.PlayerClient;
import ru.pulsecore.tournaments.exception.MessageNotFoundException;
import ru.pulsecore.tournaments.api.dto.response.ChatMessageDto;
import ru.pulsecore.tournaments.mapper.ChatMessageMapper;
import ru.pulsecore.tournaments.persistence.entity.ChatMessage;
import ru.pulsecore.tournaments.persistence.repository.ChatMessageRepository;
import ru.pulsecore.tournaments.service.outbox.OutBoxService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Pattern MENTION_PATTERN = Pattern.compile("@([\\p{L}]+)\\s+([\\p{L}]+)");


    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final OutBoxService outBoxService;
    private final PlayerClient searchByName;


    public List<PlayerSearchResponse> searchPlayers(String query) {
        if (query == null || query.isBlank()) return List.of();
        return searchByName.searchByName(query);
    }


    public List<ChatMessageDto> getMessages(Long lineupId) {
        return chatMessageRepository.findByLineupIdOrderByCreatedAtAsc(lineupId)
                .stream()
                .map(chatMessageMapper::toDto)
                .toList();
    }

    @Transactional
    public ChatMessageDto sendMessage(Long lineupId, ChatMessageDto msg) {
        ChatMessage entity = ChatMessage.builder()
                .lineupId(lineupId)
                .playerId(msg.getPlayerId())
                .playerName(msg.getPlayerName())
                .message(msg.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        if (msg.getReplyToId() != null) {
            chatMessageRepository.findById(msg.getReplyToId()).ifPresent(replyTo -> {
                entity.setReplyTo(replyTo);
                entity.setReplyToContent(replyTo.getMessage());
                entity.setReplyToSenderName(replyTo.getPlayerName());
                sendReplyPush(replyTo, msg);
            });
        }

        ChatMessage saved = chatMessageRepository.save(entity);
        ChatMessageDto result = chatMessageMapper.toDto(saved);
        processMentions(lineupId, result);

        return result;
    }

    private void sendReplyPush(ChatMessage originalMsg, ChatMessageDto replyMsg) {

        outBoxService.save(KafkaTopics.PUSH_NOTIFICATION,
                new PushNotificationEvent(
                        originalMsg.getPlayerId(),
                        "Ответ",
                        replyMsg.getPlayerName() + ": " + replyMsg.getMessage(),
                        "/live/" + originalMsg.getLineupId()

                ));
    }

    private void processMentions(Long lineupId, ChatMessageDto msg) {
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
            outBoxService.save(KafkaTopics.PUSH_NOTIFICATION,new PushNotificationEvent(
                    playerId,
                    "💬 " + msg.getPlayerName(),
                    msg.getMessage(),
                    "/live/" + lineupId
            ));
        }
    }



    @Transactional(readOnly = true)
    public List<ChatMessageDto> getMessagesAfter(Long lineupId, Long afterId) {
        return chatMessageRepository.findByIdAfterAndLineupIdOrderByCreatedAtAsc(afterId, lineupId)
                .stream()
                .map(chatMessageMapper::toDto)
                .toList();
    }

    @Transactional
    public Long deleteMessage(Long messageId, UUID playerId) {
        ChatMessage msg = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        if (!msg.getPlayerId().equals(playerId)) {
            throw new ForbiddenException("Удалять можно только свои сообщения");

        }

        Long lineupId = msg.getLineupId();
        chatMessageRepository.delete(msg);
        return lineupId;
    }

    @Transactional
    public Long updateMessage(Long messageId, UUID playerId, String newText) {
        ChatMessage msg = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        if (!msg.getPlayerId().equals(playerId)) {
            throw new ForbiddenException("Редактировать можно только свои сообщения");

        }

        msg.setMessage(newText);
        msg.setEdited(true);
        chatMessageRepository.save(msg);
        return msg.getLineupId();
    }
}