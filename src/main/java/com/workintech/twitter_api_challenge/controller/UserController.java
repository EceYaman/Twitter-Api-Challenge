package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.UserResponse;
import com.workintech.twitter_api_challenge.dto.UserUpdateRequest;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listAll() {
        List<UserResponse> list = userService.getAllUsers()
                .stream().map(UserResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication auth) {
        User u = userService.findByUsername(auth.getName());
        return ResponseEntity.ok(new UserResponse(u));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @Valid @RequestBody UserUpdateRequest req,
            Authentication auth
    ) {
        User u = userService.findByUsername(auth.getName());
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setBio(req.getBio());
        u.setProfilePhotoUrl(req.getProfilePhotoUrl());
        User updated = userService.updateUser(u.getId(), u);
        return ResponseEntity.ok(new UserResponse(updated));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(Authentication auth) {
        userService.deleteUser(userService.findByUsername(auth.getName()).getId());
        return ResponseEntity.noContent().build();
    }
}
