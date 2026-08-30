package com.fnafke.vexa.controllers.dto.friendship;

import java.util.List;

public record FriendsListDto(
        List<FriendshipDto> friends,
        int page,
        int pageSize,
        long totalElements,
        int totalPages) {
}
