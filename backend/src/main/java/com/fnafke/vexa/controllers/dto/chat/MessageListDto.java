package com.fnafke.vexa.controllers.dto.chat;

import java.util.List;

public record MessageListDto(
        List<MessageDto> messages,
        int page,
        int pageSize,
        long totalElements,
        int totalPages) {
}
