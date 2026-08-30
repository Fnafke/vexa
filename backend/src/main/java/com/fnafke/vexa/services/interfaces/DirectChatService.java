package com.fnafke.vexa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.fnafke.vexa.models.DirectChat;

public interface DirectChatService {

    /**
     * Get all direct chats for a user by their user ID.
     * 
     * @param userId The ID of the user whose direct chats are to be retrieved.
     * @return A list of DirectChat objects associated with the user.
     */
    List<DirectChat> getDirectChatsByUserId(UUID userId);

    /**
     * Get a direct chat by its ID.
     * 
     * @param directChatId The ID of the direct chat to be retrieved.
     * @return The DirectChat object with the specified ID.
     */
    DirectChat getDirectChatById(UUID directChatId);

    /**
     * Get a direct chat by its ID and the user ID of one of the participants.
     * 
     * @param directChatId The ID of the direct chat to be retrieved.
     * @param userId       The ID of one of the users in the direct chat.
     * @return The DirectChat object with the specified ID if the user is a
     *         participant.
     */
    DirectChat getDirectChatByIdAndUserId(UUID directChatId, UUID userId);

    /**
     * Get a direct chat between two users by their user IDs.
     * 
     * @param user1Id The ID of the first user.
     * @param user2Id The ID of the second user.
     * @return
     */
    DirectChat getDirectChatBetweenUsers(UUID user1Id, UUID user2Id);

    /**
     * Create a new direct chat between two users.
     * 
     * @param user1Id The ID of the first user.
     * @param user2Id The ID of the second user.
     * @return The newly created DirectChat object.
     */
    DirectChat createDirectChat(UUID user1Id, UUID user2Id);
}
