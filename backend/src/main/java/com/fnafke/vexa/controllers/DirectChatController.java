package com.fnafke.vexa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.services.interfaces.DirectChatService;

@RestController
@RequestMapping("/api/direct-chats")
public class DirectChatController {

    @Autowired
    private DirectChatService directChatService;

    public DirectChatController(DirectChatService directChatService) {
        this.directChatService = directChatService;
    }
}