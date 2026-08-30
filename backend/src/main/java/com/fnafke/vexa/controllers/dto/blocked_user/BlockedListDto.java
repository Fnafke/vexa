package com.fnafke.vexa.controllers.dto.blocked_user;

import java.util.List;

import com.fnafke.vexa.controllers.dto.user.PublicUserDto;

public record BlockedListDto(
                List<PublicUserDto> blocks,
                int page,
                int pageSize,
                long totalElements,
                int totalPages) {
}
