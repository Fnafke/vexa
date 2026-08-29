package com.fnafke.vexa.controllers.dto;

import java.util.List;

public record FriendsListDto(
        List<PublicUserDto> friends,
        int page,
        int pageSize,
        long totalElements,
        int totalPages) {
}
