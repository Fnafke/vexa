package com.fnafke.vexa.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.models.DirectChat;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.repositories.DirectChatRepository;
import com.fnafke.vexa.services.interfaces.DirectChatService;
import com.fnafke.vexa.services.interfaces.UserService;

@Service
public class DirectChatServiceImpl implements DirectChatService {

    @Autowired
    private DirectChatRepository directChatRepository;

    @Autowired
    private UserService userService;

    public DirectChatServiceImpl(DirectChatRepository directChatRepository, UserService userService) {
        this.directChatRepository = directChatRepository;
        this.userService = userService;
    }

    @Override
    public List<DirectChat> getDirectChatsByUserId(UUID userId) {
        return directChatRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    @Override
    public DirectChat getDirectChatById(UUID directChatId) {
        return directChatRepository.findById(directChatId)
                .orElseThrow(() -> new RuntimeException("Direct chat not found with ID: " + directChatId));
    }

    @Override
    public DirectChat getDirectChatByIdAndUserId(UUID directChatId, UUID userId) {
        return directChatRepository.findByIdAndUserId(directChatId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "Direct chat not found with ID: " + directChatId + " for user ID: " + userId));
    }

    @Override
    public DirectChat getDirectChatBetweenUsers(UUID user1Id, UUID user2Id) {
        return directChatRepository.findBetweenUsers(user1Id, user2Id)
                .orElseThrow(() -> new RuntimeException(
                        "Direct chat not found between users: " + user1Id + " and " + user2Id));
    }

    @Override
    public DirectChat getOrCreateDirectChat(UUID currentUserId, UUID friendId) {
        if (currentUserId.equals(friendId)) {
            throw new IllegalArgumentException("Cannot start a chat with yourself");
        }

        // Order the pair consistently, same convention as the DB constraint expects
        UUID userOneId = currentUserId.compareTo(friendId) < 0 ? currentUserId : friendId;
        UUID userTwoId = currentUserId.compareTo(friendId) < 0 ? friendId : currentUserId;

        Optional<DirectChat> existing = directChatRepository.findBetweenUsers(userOneId, userTwoId);
        if (existing.isPresent()) {
            return existing.get();
        }

        User userOne = userService.findById(userOneId);
        User userTwo = userService.findById(userTwoId);
        DirectChat newChat = new DirectChat(userOne, userTwo);
        return directChatRepository.save(newChat);
    }

    @Override
    public DirectChat createDirectChat(UUID user1Id, UUID user2Id) {
        if (directChatRepository.findBetweenUsers(user1Id, user2Id).isPresent()) {
            throw new RuntimeException("Direct chat already exists between users: " + user1Id + " and " + user2Id);
        }

        // Ensure consistent ordering of users to avoid duplicate chats
        User user1 = user1Id.compareTo(user2Id) < 0 ? userService.findById(user1Id) : userService.findById(user2Id);
        User user2 = user1Id.compareTo(user2Id) < 0 ? userService.findById(user2Id) : userService.findById(user1Id);

        DirectChat newDirectChat = new DirectChat(user1, user2);

        return directChatRepository.save(newDirectChat);
    }
}
