package com.fnafke.vexa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.fnafke.vexa.models.Friendship;

/**
 * Service interface for managing friendships between users.
 */
public interface FriendshipService {

    /**
     * Retrieves a friendship by its unique identifier.
     *
     * @param friendshipId the unique identifier of the friendship
     * @return the Friendship object if found, otherwise null
     */
    public Friendship getFriendshipById(UUID friendshipId);

    /**
     * Retrieves all friendships for a given user.
     *
     * @param userId the unique identifier of the user
     * @return a list of Friendship objects for the user
     */
    public List<Friendship> getFriendshipsByUserId(UUID userId);

    /**
     * Sends a friend request from one user to another.
     *
     * @param senderId   the unique identifier of the user sending the request
     * @param receiverId the unique identifier of the user receiving the request
     * @return the created Friendship object representing the friend request
     */
    public Friendship sendFriendRequest(UUID senderId, UUID receiverId);

    /**
     * Accepts a friend request.
     *
     * @param friendshipId the unique identifier of the friendship to accept
     * @return the updated Friendship object with status set to ACCEPTED
     */
    public Friendship acceptFriendRequest(UUID friendshipId);

    /**
     * Declines a friend request.
     *
     * @param friendshipId the unique identifier of the friendship to decline
     * @return the updated Friendship object with status set to DECLINED
     */
    public Friendship declineFriendRequest(UUID friendshipId);

    /**
     * Removes a friendship between two users.
     *
     * @param friendshipId the unique identifier of the friendship to remove
     */
    public void removeFriendship(UUID friendshipId);
}
