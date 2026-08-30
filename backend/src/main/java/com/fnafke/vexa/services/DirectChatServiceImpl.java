package com.fnafke.vexa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fnafke.vexa.repositories.DirectChatRepository;
import com.fnafke.vexa.services.interfaces.DirectChatService;

@Service
public class DirectChatServiceImpl implements DirectChatService {

    @Autowired
    private DirectChatRepository directChatRepository;

    public DirectChatServiceImpl(DirectChatRepository directChatRepository) {
        this.directChatRepository = directChatRepository;
    }
}
