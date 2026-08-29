package com.fnafke.vexa.services.interfaces;

import com.fnafke.vexa.models.BlockedUser;
import com.fnafke.vexa.models.User;

/**
 * Service interface for managing blocked users.
 */
public interface BlockedUserService {
    /**
     * Checks if a user is blocked by another user.
     *
     * @param blocker the user who may have blocked another user
     * @param blocked the user who may be blocked
     * @return true if the user is blocked, false otherwise
     */
    boolean isUserBlockedBy(User blocker, User blocked);

    /**
     * Blocks a user.
     *
     * @param blocker the user who is blocking another user
     * @param blocked the user to be blocked
     * @return the BlockedUser object representing the blocked user
     */
    BlockedUser blockUser(User blocker, User blocked);

    /**
     * Unblocks a user.
     *
     * @param blockerId the unique identifier of the user who is unblocking another
     *                  user
     * @param blockedId the unique identifier of the user to be unblocked
     * @return a message indicating the result of the unblocking operation
     */
    String unblockUser(User blocker, User blocked);
}
