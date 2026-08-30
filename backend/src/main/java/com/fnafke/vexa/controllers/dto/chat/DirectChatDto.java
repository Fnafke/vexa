package com.fnafke.vexa.controllers.dto.chat;

import java.time.Instant;
import java.util.UUID;

import com.fnafke.vexa.controllers.dto.user.PublicUserDto;
import com.fnafke.vexa.models.DirectChat;

public record DirectChatDto(
        UUID id,
        PublicUserDto user,
        Instant createdAt,
        Instant updatedAt) {
    public static DirectChatDto fromDirectChat(
            DirectChat directChat, UUID currentUserId) {
        PublicUserDto userDto = directChat.getUserOne().getId().equals(currentUserId)
                ? PublicUserDto.fromUser(directChat.getUserTwo())
                : PublicUserDto.fromUser(directChat.getUserOne());
        return new DirectChatDto(
                directChat.getId(),
                userDto,
                directChat.getCreatedAt(),
                directChat.getUpdatedAt());
    }
}
