package com.fnafke.vexa.services.interfaces;

import java.util.UUID;

import com.fnafke.vexa.models.Chat;

public interface ChatService {

    /**
     * Finds a chat by its ID.
     * 
     * @param chatId The ID of the chat to find.
     * @return The Chat object with the specified ID, or null if not found.
     */
    Chat findById(UUID chatId);

    /**
     * Checks if a user is a participant in a chat.
     * 
     * @param chatId The ID of the chat to check.
     * @param userId The ID of the user to check.
     * @return true if the user is a participant in the chat, false otherwise.
     */
    boolean isParticipant(Chat chat, UUID userId);
}
