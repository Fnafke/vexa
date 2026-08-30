package com.fnafke.vexa.controllers.dto.chat;

import java.time.Instant;
import java.util.UUID;

import com.fnafke.vexa.controllers.dto.user.PublicUserDto;
import com.fnafke.vexa.models.DirectChat;

public record DirectChatDto(
        UUID id,
        PublicUserDto userOne,
        PublicUserDto userTwo,
        Instant createdAt,
        Instant updatedAt) {
    public static DirectChatDto fromDirectChat(
            DirectChat directChat) {
        return new DirectChatDto(
                directChat.getId(),
                PublicUserDto.fromUser(directChat.getUserOne()),
                PublicUserDto.fromUser(directChat.getUserTwo()),
                directChat.getCreatedAt(),
                directChat.getUpdatedAt());
    }
}
