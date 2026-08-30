package com.fnafke.vexa.services.interfaces;

import java.util.UUID;

import com.fnafke.vexa.controllers.dto.chat.MessageListDto;
import com.fnafke.vexa.models.Message;
import com.fnafke.vexa.models.User;

public interface MessageService {

    /**
     * Get messages by chat ID with pagination.
     * 
     * @param chatId   The ID of the chat
     * @param userId   The ID of the user
     * @param page     The page number
     * @param pageSize The number of items per page
     * @return The list of messages
     */
    MessageListDto getMessagesByChatId(UUID chatId, UUID userId, int page, int pageSize);

    /**
     * Send a message in a chat.
     * 
     * @param chatId  The ID of the chat
     * @param userId  The ID of the user sending the message
     * @param content The content of the message
     * @return The sent message
     */
    Message sendMessage(UUID chatId, User user, String content);
}
