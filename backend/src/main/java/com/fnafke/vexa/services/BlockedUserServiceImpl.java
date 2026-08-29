package com.fnafke.vexa.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.models.BlockedUser;
import com.fnafke.vexa.models.Friendship;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.repositories.BlockedUserRepository;
import com.fnafke.vexa.services.interfaces.BlockedUserService;
import com.fnafke.vexa.services.interfaces.FriendshipService;
import com.fnafke.vexa.services.interfaces.UserService;

@Service
public class BlockedUserServiceImpl implements BlockedUserService {

    @Autowired
    private BlockedUserRepository blockedUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    public BlockedUserServiceImpl(
            BlockedUserRepository blockedUserRepository,
            UserService userService,
            FriendshipService friendshipService) {
        this.blockedUserRepository = blockedUserRepository;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @Override
    public boolean isUserBlockedBy(UUID blockerId, UUID blockedId) {
        return blockedUserRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Override
    public BlockedUser blockUser(UUID blockerId, UUID blockedId) {
        User blocker = userService.findById(blockerId);
        User blocked = userService.findById(blockedId);

        if (isUserBlockedBy(blockerId, blockedId)) {
            throw new IllegalArgumentException("User is already blocked.");
        }

        Friendship friendship = friendshipService.getFriendshipBetweenUsers(blockerId, blockedId);
        if (friendship != null) {
            this.friendshipService.removeFriendship(friendship.getId());
        }

        BlockedUser blockedUser = new BlockedUser(blocker, blocked);
        blockedUserRepository.save(blockedUser);

        return blockedUser;
    }

    @Override
    public String unblockUser(UUID blockerId, UUID blockedId) {
        User blocker = userService.findById(blockerId);
        User blocked = userService.findById(blockedId);

        BlockedUser blockedUser = blockedUserRepository.findBetweenUsers(blockerId, blockedId)
                .orElseThrow(() -> new IllegalArgumentException("User is not blocked."));

        blockedUserRepository.delete(blockedUser);

        return "User " + blocked.getUsername() + " has been unblocked by " + blocker.getUsername() + ".";
    }

}
