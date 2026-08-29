package com.fnafke.vexa.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<BlockedUser> getBlockedUsersByBlockerId(UUID blockerId, Pageable pageable) {
        return blockedUserRepository.findByBlockerId(blockerId, pageable);
    }

    @Override
    public boolean isUserBlockedBy(User blocker, User blocked) {
        return blockedUserRepository.existsByBlockerIdAndBlockedId(blocker.getId(), blocked.getId());
    }

    @Override
    public BlockedUser blockUser(User blocker, User blocked) {

        BlockedUser blockedUser = new BlockedUser(blocker, blocked);
        blockedUserRepository.save(blockedUser);

        return blockedUser;
    }

    @Override
    public String unblockUser(User blocker, User blocked) {

        BlockedUser blockedUser = blockedUserRepository.findBetweenUsers(blocker.getId(), blocked.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not blocked."));

        blockedUserRepository.delete(blockedUser);

        return "User " + blocked.getUsername() + " has been unblocked by " + blocker.getUsername() + ".";
    }

}
