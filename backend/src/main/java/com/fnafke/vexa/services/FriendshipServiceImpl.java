package com.fnafke.vexa.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.controllers.dto.blocked_user.BlockedListDto;
import com.fnafke.vexa.controllers.dto.friendship.FriendsListDto;
import com.fnafke.vexa.controllers.dto.friendship.FriendshipDto;
import com.fnafke.vexa.controllers.dto.user.PublicUserDto;
import com.fnafke.vexa.models.BlockedUser;
import com.fnafke.vexa.models.Friendship;
import com.fnafke.vexa.models.FriendshipStatus;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.models.exceptions.NotFoundException;
import com.fnafke.vexa.repositories.FriendshipRepository;
import com.fnafke.vexa.services.interfaces.BlockedUserService;
import com.fnafke.vexa.services.interfaces.FriendshipService;
import com.fnafke.vexa.services.interfaces.UserService;

@Service
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
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return friendshipRepository.findByUserId(userId);
    }

    @Override
    public Friendship getFriendshipBetweenUsers(UUID userAId, UUID userBId) {
        if (!userService.existsById(userAId)) {
            throw new NotFoundException("User A not found with ID: " + userAId);
        }
        if (!userService.existsById(userBId)) {
            throw new NotFoundException("User B not found with ID: " + userBId);
        }

        return friendshipRepository.findBetweenUsers(userAId, userBId).orElse(null);
    }

    @Override
    public FriendsListDto getFriendsListByUserIdAndStatus(UUID userId, FriendshipStatus status, int page,
            int pageSize) {
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<Friendship> friendshipPage = friendshipRepository.findAllByUserIdAndStatus(userId, status, pageable);

        List<FriendshipDto> friends = friendshipPage.getContent().stream()
                .map(FriendshipDto::fromFriendship)
                .toList();

        return new FriendsListDto(
                friends,
                friendshipPage.getNumber(),
                friendshipPage.getSize(),
                friendshipPage.getTotalElements(),
                friendshipPage.getTotalPages());
    }

    @Override
    public BlockedListDto getBlockedListByUserId(UUID userId, int page, int pageSize) {
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<BlockedUser> blockedUsers = blockedUserService.getBlockedUsersByBlockerId(userId, pageable);

        List<PublicUserDto> blocks = blockedUsers.getContent().stream()
                .map(b -> PublicUserDto.fromUser(b.getBlocked()))
                .toList();

        return new BlockedListDto(
                blocks,
                page,
                pageSize,
                blockedUsers.getTotalElements(),
                blockedUsers.getTotalPages());
    }

    @Override
    public Friendship sendFriendRequest(String senderUsername, String receiverUsername) {
        if (senderUsername.equals(receiverUsername)) {
            throw new IllegalArgumentException("Cannot send friend request to yourself.");
        }

        User userA = userService.findByUsername(senderUsername);
        User userB = userService.findByUsername(receiverUsername);

        if (blockedUserService.isUserBlockedBy(userB, userA)) {
            throw new IllegalArgumentException("Cannot send friend request. You are blocked by this user.");
        }

        Friendship existingFriendship = this.getFriendshipBetweenUsers(userA.getId(), userB.getId());

        if (existingFriendship != null) {

            switch (existingFriendship.getStatus()) {
                case PENDING -> throw new IllegalArgumentException("Friend request is already pending.");
                case ACCEPTED -> throw new IllegalArgumentException("You are already friends.");
                case DECLINED -> {
                    existingFriendship.setStatus(FriendshipStatus.PENDING);

                    // Swap requester and addressee to allow the sender to send a new request
                    existingFriendship.setRequester(userA);
                    existingFriendship.setAddressee(userB);
                    return friendshipRepository.save(existingFriendship);
                }
            }
        }

        Friendship friendship = new Friendship(userA, userB);
        return friendshipRepository.save(friendship);
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
        return friendshipRepository.save(friendship);
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
        return friendshipRepository.save(friendship);
    }

    @Override
    public void removeFriendship(UUID friendshipId) {
        Friendship friendship = this.getFriendshipById(friendshipId);
        if (friendship == null) {
            throw new IllegalArgumentException("There is no friendship with the provided ID.");
        }
        if (friendship.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new IllegalArgumentException("Cannot remove a friendship that is not accepted.");
        }
        friendship.setStatus(FriendshipStatus.DECLINED);
        friendshipRepository.save(friendship);
    }

    @Override
    public BlockedUser blockUser(UUID blockerId, UUID blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("Cannot block yourself.");
        }

        User blocker = userService.findById(blockerId);
        User blocked = userService.findById(blockedId);

        if (blockedUserService.isUserBlockedBy(blocker, blocked)) {
            throw new IllegalArgumentException("User is already blocked.");
        }

        Friendship existingFriendship = this.getFriendshipBetweenUsers(blockerId, blockedId);
        if (existingFriendship != null) {
            friendshipRepository.delete(existingFriendship);
        }

        return blockedUserService.blockUser(blocker, blocked);
    }

    @Override
    public String unblockUser(UUID blockerId, UUID blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("Cannot unblock yourself.");
        }

        User blocker = userService.findById(blockerId);
        User blocked = userService.findById(blockedId);

        if (!blockedUserService.isUserBlockedBy(blocker, blocked)) {
            throw new IllegalArgumentException("User is not blocked.");
        }

        return blockedUserService.unblockUser(blocker, blocked);
    }
}
