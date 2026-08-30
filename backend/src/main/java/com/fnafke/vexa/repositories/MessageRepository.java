package com.fnafke.vexa.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fnafke.vexa.models.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.chat.id = :chatId " +
            "ORDER BY m.createdAt DESC")
    Page<Message> findByChatIdOrderByCreatedAtDesc(@Param("chatId") UUID chatId, Pageable pageable);
}
