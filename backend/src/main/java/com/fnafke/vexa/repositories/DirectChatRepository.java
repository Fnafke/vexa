package com.fnafke.vexa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnafke.vexa.models.DirectChat;

public interface DirectChatRepository extends JpaRepository<DirectChat, UUID> {

}
