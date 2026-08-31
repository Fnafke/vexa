package com.fnafke.vexa.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.controllers.dto.chat.MessageDto;
import com.fnafke.vexa.controllers.dto.chat.MessageListDto;
import com.fnafke.vexa.models.Chat;
import com.fnafke.vexa.models.Message;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.repositories.MessageRepository;
import com.fnafke.vexa.services.interfaces.ChatService;
import com.fnafke.vexa.services.interfaces.MessageService;

import jakarta.transaction.Transactional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    public MessageServiceImpl(MessageRepository messageRepository, ChatService chatService,
            SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public MessageListDto getMessagesByChatId(UUID chatId, UUID userId, int page, int pageSize) {
        Chat chat = chatService.findById(chatId);

        if (!chatService.isParticipant(chat, userId)) {
            throw new AccessDeniedException("User is not a participant in the chat");
        }
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<Message> messagePage = messageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);

        List<MessageDto> messages = messagePage.getContent().stream()
                .map(MessageDto::fromEntity)
                .toList();

        return new MessageListDto(
                messages,
                messagePage.getNumber(),
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.getTotalPages());
    }

    @Override
    @Transactional
    public Message sendMessage(UUID chatId, User user, String content) {
        Chat chat = chatService.findById(chatId);

        if (!chatService.isParticipant(chat, user.getId())) {
            throw new AccessDeniedException("User is not a participant in the chat");
        }

        Message message = new Message(user, chat, content);
        Message savedMessage = messageRepository.save(message);

        MessageDto messageDto = MessageDto.fromEntity(savedMessage);

        try {
            messagingTemplate.convertAndSend("/topic/chats/" + chatId, messageDto);
        } catch (Exception ex) {
            logger.warn("Failed to broadcast message {} to chat {}", savedMessage.getId(), chatId, ex);
        }

        return savedMessage;
    }
}
