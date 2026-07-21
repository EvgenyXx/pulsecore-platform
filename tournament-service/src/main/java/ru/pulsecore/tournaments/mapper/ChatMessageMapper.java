package ru.pulsecore.tournaments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pulsecore.tournaments.api.dto.response.ChatMessageDto;
import ru.pulsecore.tournaments.persistence.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "replyToId", expression = "java(entity.getReplyTo() != null ? entity.getReplyTo().getId() : null)")
    ChatMessageDto toDto(ChatMessage entity);
}