package com.fnafke.vexa.controllers.dto.chat;

import java.time.Instant;
import java.util.UUID;

import com.fnafke.vexa.controllers.dto.user.PublicUserDto;
import com.fnafke.vexa.models.Message;

public record MessageDto(
        UUID id,
        PublicUserDto sender,
        String content,
        Instant createdAt,
        Instant updatedAt) {
    public static MessageDto fromEntity(Message message) {
        return new MessageDto(
                message.getId(),
                PublicUserDto.fromUser(message.getSender()),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt());
    }
}
