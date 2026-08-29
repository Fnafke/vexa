package com.fnafke.vexa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.controllers.dto.FriendsListDto;
import com.fnafke.vexa.models.FriendshipStatus;
import com.fnafke.vexa.models.User;
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

    // /api/friendships?status={ACCEPTED}?page={0}&size={10}
    @GetMapping()
    public ResponseEntity<FriendsListDto> getFriendshipsByUserAndStatus(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(name = "status", required = true) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        FriendsListDto friendsList = friendshipService.getFriendsListByUserIdAndStatus(currentUser.getId(),
                FriendshipStatus.valueOf(status),
                page,
                size);
        return ResponseEntity.ok(friendsList);
    }

    // POST Mappings
}
