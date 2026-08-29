package com.fnafke.vexa.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.fnafke.vexa.models.Friendship;
import com.fnafke.vexa.models.FriendshipStatus;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.models.exceptions.NotFoundException;
import com.fnafke.vexa.repositories.FriendshipRepository;
import com.fnafke.vexa.services.interfaces.BlockedUserService;
import com.fnafke.vexa.services.interfaces.FriendshipService;
import com.fnafke.vexa.services.interfaces.UserService;

public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlockedUserService blockedUserService;

    public FriendshipServiceImpl(
            FriendshipRepository friendshipRepository,
            UserService userService,
            BlockedUserService blockedUserService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
        this.blockedUserService = blockedUserService;
    }

    @Override
    public Friendship getFriendshipById(UUID friendshipId) {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NotFoundException("Friendship not found with ID: " + friendshipId));
    }

    @Override
    public List<Friendship> getFriendshipsByUserId(UUID userId) {
        User user = userService.findById(userId);
        return friendshipRepository.findByUserId(userId);
    }

    @Override
    public Friendship getFriendshipBetweenUsers(UUID userAId, UUID userBId) {
        User userA = userService.findById(userAId);
        User userB = userService.findById(userBId);

        return friendshipRepository.findBetweenUsers(userAId, userBId).orElse(null);
    }

    @Override
    public Friendship sendFriendRequest(UUID senderId, UUID receiverId) {
        User userA = userService.findById(senderId);
        User userB = userService.findById(receiverId);

        if (blockedUserService.isUserBlockedBy(receiverId, senderId)) {
            throw new IllegalArgumentException("Cannot send friend request. You are blocked by this user.");
        }

        Friendship existingFriendship = this.getFriendshipBetweenUsers(senderId, receiverId);
        if (existingFriendship != null && existingFriendship.getStatus() == FriendshipStatus.ACCEPTED) {
            throw new IllegalArgumentException("Friend request already accepted.");
        }
        if (existingFriendship != null && existingFriendship.getStatus() == FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friend request already sent.");
        }

        Friendship friendship = new Friendship(userA, userB);
        friendshipRepository.save(friendship);
        return friendship;
    }

    @Override
    public Friendship acceptFriendRequest(UUID friendshipId) {
        Friendship friendship = this.getFriendshipById(friendshipId);
        if (friendship == null) {
            throw new IllegalArgumentException("There is no friend request with the provided ID.");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending.");
        }
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
        return friendship;
    }

    @Override
    public Friendship declineFriendRequest(UUID friendshipId) {
        Friendship friendship = this.getFriendshipById(friendshipId);
        if (friendship == null) {
            throw new IllegalArgumentException("There is no friend request with the provided ID.");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending.");
        }
        friendship.setStatus(FriendshipStatus.DECLINED);
        friendshipRepository.save(friendship);
        return friendship;
    }

    @Override
    public void removeFriendship(UUID friendshipId) {
        Friendship friendship = this.getFriendshipById(friendshipId);
        if (friendship == null) {
            throw new IllegalArgumentException("There is no friendship with the provided ID.");
        }
        friendshipRepository.delete(friendship);
    }
}
