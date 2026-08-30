package com.fnafke.vexa.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fnafke.vexa.models.DirectChat;

public interface DirectChatRepository extends JpaRepository<DirectChat, UUID> {

    @Query("SELECT dc FROM DirectChat dc WHERE " +
            "(dc.userOne.id = :userAId AND dc.userTwo.id = :userBId) OR " +
            "(dc.userOne.id = :userBId AND dc.userTwo.id = :userAId)")
    Optional<DirectChat> findBetweenUsers(@Param("userAId") UUID userAId, @Param("userBId") UUID userBId);

    @Query("SELECT dc FROM DirectChat dc WHERE " +
            "dc.userOne.id = :userId OR dc.userTwo.id = :userId")
    List<DirectChat> findByUserId(@Param("userId") UUID userId);
}
