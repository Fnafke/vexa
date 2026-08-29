package com.fnafke.vexa.controllers.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fnafke.vexa.models.User;

public record PublicUserDto(
        UUID id,
        String username,
        LocalDate createdAt) {
    public static PublicUserDto fromUser(User user) {
        return new PublicUserDto(user.getId(), user.getUsername(), user.getCreatedAt());
    }
}
