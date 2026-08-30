package com.fnafke.vexa.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.models.Chat;
import com.fnafke.vexa.models.DirectChat;
import com.fnafke.vexa.repositories.ChatRepository;
import com.fnafke.vexa.services.interfaces.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat findById(UUID chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found with ID: " + chatId));
    }

    @Override
    public boolean isParticipant(Chat chat, UUID userId) {

        if (chat instanceof DirectChat directChat) {
            return directChat.getUserOne().getId().equals(userId) || directChat.getUserTwo().getId().equals(userId);
        }

        return false;
    }
}
