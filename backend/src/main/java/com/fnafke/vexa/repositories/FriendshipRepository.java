package com.fnafke.vexa.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fnafke.vexa.models.Friendship;
import com.fnafke.vexa.models.FriendshipStatus;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

        @Query("SELECT f FROM Friendship f WHERE " +
                        "(f.requester.id = :userAId AND f.addressee.id = :userBId) OR " +
                        "(f.requester.id = :userBId AND f.addressee.id = :userAId)")
        Optional<Friendship> findBetweenUsers(@Param("userAId") UUID userAId,
                        @Param("userBId") UUID userBId);

        @Query("SELECT f FROM Friendship f WHERE " +
                        "f.requester.id = :userId OR f.addressee.id = :userId")
        List<Friendship> findByUserId(@Param("userId") UUID userId);

        @Query("SELECT f FROM Friendship f " +
                        "JOIN FETCH f.requester " +
                        "JOIN FETCH f.addressee " +
                        "WHERE f.status = :status AND (f.requester.id = :userId OR f.addressee.id = :userId)")
        Page<Friendship> findAllByUserIdAndStatus(@Param("userId") UUID userId,
                        @Param("status") FriendshipStatus status,
                        Pageable pageable);
}
