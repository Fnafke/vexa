package com.fnafke.vexa.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.controllers.dto.AcceptFriendRequestDto;
import com.fnafke.vexa.controllers.dto.BlockedListDto;
import com.fnafke.vexa.controllers.dto.DeclineFriendRequestDto;
import com.fnafke.vexa.controllers.dto.FriendRequestDto;
import com.fnafke.vexa.controllers.dto.FriendsListDto;
import com.fnafke.vexa.controllers.dto.FriendshipDto;
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

    // /api/friendships/blocked
    @GetMapping("/blocked")
    public ResponseEntity<BlockedListDto> getUserBlockedList(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        BlockedListDto blockedList = friendshipService.getBlockedListByUserId(currentUser.getId(), page, size);
        return ResponseEntity.ok(blockedList);
    }

    // POST Mappings

    // /api/friendships/request/send
    @PostMapping("/request/send")
    public ResponseEntity<FriendshipDto> sendFriendRequest(
            @AuthenticationPrincipal User currentUser,
            @RequestBody FriendRequestDto friendRequestDto) {

        FriendshipDto friendshipDto = FriendshipDto.fromFriendship(
                this.friendshipService.sendFriendRequest(currentUser.getUsername(),
                        friendRequestDto.receiverUsername()));

        return ResponseEntity.status(201).body(friendshipDto);
    }

    // /api/friendships/request/accept
    @PostMapping("/request/accept")
    public ResponseEntity<FriendshipDto> acceptFriendRequest(
            @AuthenticationPrincipal User currentUser,
            @RequestBody AcceptFriendRequestDto acceptFriendRequestDto) {

        FriendshipDto friendshipDto = FriendshipDto.fromFriendship(
                this.friendshipService.acceptFriendRequest(acceptFriendRequestDto.friendshipId()));

        return ResponseEntity.status(201).body(friendshipDto);
    }

    // /api/friendships/request/decline
    @PostMapping("/request/decline")
    public ResponseEntity<FriendshipDto> declineFriendRequest(
            @AuthenticationPrincipal User currentUser,
            @RequestBody DeclineFriendRequestDto declineFriendRequestDto) {

        FriendshipDto friendshipDto = FriendshipDto.fromFriendship(
                this.friendshipService.declineFriendRequest(declineFriendRequestDto.friendshipId()));

        return ResponseEntity.status(201).body(friendshipDto);
    }
}
