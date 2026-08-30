package com.fnafke.vexa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.services.interfaces.MessageService;

@RestController
@RequestMapping("/api/chats/{chatId}/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
}
