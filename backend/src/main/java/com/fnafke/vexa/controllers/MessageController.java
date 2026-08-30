package com.fnafke.vexa.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.controllers.dto.chat.MessageDto;
import com.fnafke.vexa.controllers.dto.chat.MessageListDto;
import com.fnafke.vexa.controllers.dto.chat.SendMessageRequest;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.services.interfaces.MessageService;

@RestController
@RequestMapping("/api/chats/{chatId}/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // GET Requests

    // /api/chats/{chatId}/messages - Get all messages for a specific chat
    @GetMapping
    public MessageListDto getMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return messageService.getMessagesByChatId(chatId, currentUser.getId(), page, pageSize);
    }

    // POST Requests
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID chatId,
            @RequestBody SendMessageRequest request) {
        MessageDto message = MessageDto.fromEntity(messageService.sendMessage(chatId, currentUser, request.content()));
        return ResponseEntity.status(201).body(message);
    }
}
