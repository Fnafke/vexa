package com.fnafke.vexa.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnafke.vexa.controllers.dto.user.UserDto;
import com.fnafke.vexa.models.User;
import com.fnafke.vexa.services.interfaces.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    // GET MAPPINGS

    // /api/users/me - Get the currently authenticated user's information
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        UserDto userDto = new UserDto(currentUser);
        return ResponseEntity.ok(userDto);
    }

    // /api/users/{id} - Get a user's information by their ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(new UserDto(userService.findById(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST MAPPINGS

}
