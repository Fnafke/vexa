package com.fnafke.vexa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.services.interfaces.FriendshipService;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipsController {

    @Autowired
    private FriendshipService friendshipService;

    public FriendshipsController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // GET Mappings

    // POST Mappings
}
