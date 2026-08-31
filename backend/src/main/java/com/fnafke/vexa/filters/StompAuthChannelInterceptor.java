package com.fnafke.vexa.filters;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.fnafke.vexa.models.Chat;
import com.fnafke.vexa.services.interfaces.ChatService;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String CHAT_TOPIC_PATTERN = "/topic/chats/{chatId}";

    @Autowired
    private ChatService chatService;

    public StompAuthChannelInterceptor(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticateConnection(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeSubscription(accessor);
        }

        return message;
    }

    public void authenticateConnection(StompHeaderAccessor accessor) {
        Object userId = accessor.getSessionAttributes().get("userId");
        Object username = accessor.getSessionAttributes().get("username");

        if (userId == null || username == null) {
            throw new MessagingException(
                    "Unauthenticated STOMP connection");
        }

        Principal principal = () -> username.toString();
        accessor.setUser(principal);
    }

    public void authorizeSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null) {
            throw new MessagingException("Subscription destination is null");
        }

        if (!PATH_MATCHER.match(CHAT_TOPIC_PATTERN, destination)) {
            return;
        }

        UUID chatId = UUID.fromString(
                PATH_MATCHER.extractUriTemplateVariables(CHAT_TOPIC_PATTERN, destination).get("chatId"));

        Object userIdObj = accessor.getSessionAttributes() != null
                ? accessor.getSessionAttributes().get("userId")
                : null;

        if (userIdObj == null) {
            throw new MessagingException("Unauthenticated STOMP subscription attempt");
        }

        UUID userId = (UUID) userIdObj;

        Chat chat = chatService.findById(chatId);

        if (!chatService.isParticipant(chat, userId)) {
            throw new MessagingException("Not authorized to subscribe to chat: " + chatId);
        }
    }
}