package com.fnafke.vexa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fnafke.vexa.models.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
}
