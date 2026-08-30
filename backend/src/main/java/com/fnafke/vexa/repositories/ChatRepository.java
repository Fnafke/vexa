package com.fnafke.vexa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnafke.vexa.models.Chat;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

}
