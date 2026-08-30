package com.fnafke.vexa.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.controllers.dto.chat.CreateDirectChatDto;
import com.fnafke.vexa.controllers.dto.chat.DirectChatDto;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.services.interfaces.DirectChatService;

@RestController
@RequestMapping("/api/direct-chats")
public class DirectChatController {

    @Autowired
    private DirectChatService directChatService;

    public DirectChatController(DirectChatService directChatService) {
        this.directChatService = directChatService;
    }

    // GET Requests

    // /api/direct-chats - Get all direct chats for the authenticated user
    @GetMapping()
    public ResponseEntity<List<DirectChatDto>> getAllDirectChatsForUser(@AuthenticationPrincipal User currentUser) {
        List<DirectChatDto> directChats = directChatService.getDirectChatsByUserId(currentUser.getId())
                .stream()
                .map(directChat -> DirectChatDto.fromDirectChat(directChat))
                .toList();
        return ResponseEntity.ok(directChats);
    }

    // /api/direct-chats/{directChatId} - Get a specific direct chat by its ID
    @GetMapping("/{directChatId}")
    public ResponseEntity<DirectChatDto> getDirectChatById(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID directChatId) {
        DirectChatDto directChatDto = DirectChatDto.fromDirectChat(
                directChatService.getDirectChatByIdAndUserId(directChatId, currentUser.getId()));
        return ResponseEntity.ok(directChatDto);
    }

    // POST Requests

    // /api/direct-chats - Create a new direct chat between the authenticated user
    // and another user
    @PostMapping()
    public ResponseEntity<DirectChatDto> createDirectChat(@AuthenticationPrincipal User currentUser,
            @RequestBody CreateDirectChatDto request) {
        DirectChatDto directChatDto = DirectChatDto.fromDirectChat(
                directChatService.createDirectChat(currentUser.getId(), request.userId()));
        return ResponseEntity.created(null).body(directChatDto);
    }
}