package com.fnafke.vexa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnafke.vexa.models.BlockedUser;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, UUID> {
    boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

    void deleteByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);
}
