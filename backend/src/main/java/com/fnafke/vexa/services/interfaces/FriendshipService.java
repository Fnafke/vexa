package com.fnafke.vexa.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.fnafke.vexa.controllers.dto.blocked_user.BlockedListDto;
import com.fnafke.vexa.controllers.dto.friendship.FriendsListDto;
import com.fnafke.vexa.models.BlockedUser;
import com.fnafke.vexa.models.Friendship;
import com.fnafke.vexa.models.FriendshipStatus;
import com.fnafke.vexa.models.exceptions.NotFoundException;

/**
 * Service interface for managing friendships between users.
 */
public interface FriendshipService {

    /**
     * Retrieves a friendship by its unique identifier.
     *
     * @param friendshipId the unique identifier of the friendship
     * @return the Friendship object if found, otherwise null
     * @throws NotFoundException if the friendship is not found
     */
    public Friendship getFriendshipById(UUID friendshipId) throws NotFoundException;

    /**
     * Retrieves all friendships for a given user.
     *
     * @param userId the unique identifier of the user
     * @return a list of Friendship objects for the user
     */
    public List<Friendship> getFriendshipsByUserId(UUID userId);

    /**
     * Retrieves the friendship between two users, if it exists.
     *
     * @param userAId the unique identifier of the first user
     * @param userBId the unique identifier of the second user
     * @return the Friendship object if found, otherwise null
     */
    public Friendship getFriendshipBetweenUsers(UUID userAId, UUID userBId);

    /**
     * Retrieves a list of friends for a given user based on their status.
     *
     * @param userId   the unique identifier of the user
     * @param status   the status of the friendships to retrieve (e.g., ACCEPTED)
     * @param page     the page number
     * @param pageSize the number of items per page
     * @return a FriendsListDto containing the friends and pagination
     *         information
     */
    public FriendsListDto getFriendsListByUserIdAndStatus(UUID userId, FriendshipStatus status, int page, int pageSize);

    /**
     * Retrieves a list of blocked users for a given user.
     *
     * @param userId   the unique identifier of the user
     * @param page     the page number
     * @param pageSize the number of items per page
     * @return a BlockedListDto containing the blocked users and pagination
     *         information
     */
    public BlockedListDto getBlockedListByUserId(UUID userId, int page, int pageSize);

    /**
     * Sends a friend request from one user to another.
     *
     * @param senderUsername   the username of the user sending the request
     * @param receiverUsername the username of the user receiving the request
     * @return the created Friendship object representing the friend request
     */
    public Friendship sendFriendRequest(String senderUsername, String receiverUsername);

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

    /**
     * Blocks a user.
     *
     * @param blockerId the unique identifier of the user who is blocking another
     *                  user
     * @param blockedId the unique identifier of the user to be blocked
     * @return the BlockedUser object representing the blocked user
     */
    public BlockedUser blockUser(UUID blockerId, UUID blockedId);

    /**
     * Unblocks a user.
     *
     * @param blockerId the unique identifier of the user who is unblocking another
     *                  user
     * @param blockedId the unique identifier of the user to be unblocked
     * @return a message indicating the result of the unblocking operation
     */
    public String unblockUser(UUID blockerId, UUID blockedId);
}
