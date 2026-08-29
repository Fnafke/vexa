package com.fnafke.vexa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fnafke.vexa.models.BlockedUser;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, UUID> {
    boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

    @Query("SELECT b FROM BlockedUser b WHERE " +
            "(b.blocker.id = :userAId AND b.blocked.id = :userBId) OR " +
            "(b.blocker.id = :userBId AND b.blocked.id = :userAId)")
    Optional<BlockedUser> findBetweenUsers(@Param("userAId") UUID userAId, @Param("userBId") UUID userBId);

    void deleteByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);
}
