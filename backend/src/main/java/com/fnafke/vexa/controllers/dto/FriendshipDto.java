package com.fnafke.vexa.controllers.dto;

import java.time.Instant;
import java.util.UUID;

import com.fnafke.vexa.models.Friendship;
import com.fnafke.vexa.models.FriendshipStatus;

public record FriendshipDto(
        UUID id,
        PublicUserDto requester,
        PublicUserDto addressee,
        FriendshipStatus status,
        Instant createdAt) {
    public static FriendshipDto fromFriendship(
            Friendship friendship) {
        return new FriendshipDto(
                friendship.getId(),
                PublicUserDto.fromUser(friendship.getRequester()),
                PublicUserDto.fromUser(friendship.getAddressee()),
                friendship.getStatus(),
                friendship.getCreatedAt());
    }
}
