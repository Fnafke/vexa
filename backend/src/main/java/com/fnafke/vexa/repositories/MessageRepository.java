package com.fnafke.vexa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnafke.vexa.models.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {

}
