package com.fnafke.vexa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.models.BlockedUser;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.repositories.BlockedUserRepository;
import com.fnafke.vexa.services.interfaces.BlockedUserService;

@Service
public class BlockedUserServiceImpl implements BlockedUserService {

    @Autowired
    private BlockedUserRepository blockedUserRepository;

    public BlockedUserServiceImpl(
            BlockedUserRepository blockedUserRepository) {
        this.blockedUserRepository = blockedUserRepository;
    }

    @Override
    public boolean isUserBlockedBy(User blocker, User blocked) {
        return blockedUserRepository.existsByBlockerIdAndBlockedId(blocker.getId(), blocked.getId());
    }

    @Override
    public BlockedUser blockUser(User blocker, User blocked) {

        if (isUserBlockedBy(blocker, blocked)) {
            throw new IllegalArgumentException("User is already blocked.");
        }

        BlockedUser blockedUser = new BlockedUser(blocker, blocked);
        blockedUserRepository.save(blockedUser);

        return blockedUser;
    }

    @Override
    public String unblockUser(User blocker, User blocked) {
        if (!isUserBlockedBy(blocker, blocked)) {
            throw new IllegalArgumentException("User is not blocked.");
        }

        BlockedUser blockedUser = blockedUserRepository.findBetweenUsers(blocker.getId(), blocked.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not blocked."));

        blockedUserRepository.delete(blockedUser);

        return "User " + blocked.getUsername() + " has been unblocked by " + blocker.getUsername() + ".";
    }

}
