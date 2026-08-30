package com.fnafke.vexa.controllers.dto.user;

import java.time.Instant;
import java.util.UUID;

import com.fnafke.vexa.models.User;

public record PublicUserDto(
        UUID id,
        String username,
        Instant createdAt) {
    public static PublicUserDto fromUser(User user) {
        return new PublicUserDto(user.getId(), user.getUsername(), user.getCreatedAt());
    }
}
