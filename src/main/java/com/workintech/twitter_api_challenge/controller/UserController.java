package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.UserResponse;
import com.workintech.twitter_api_challenge.dto.UserUpdateRequest;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<UserResponse> listAll() {
        return userService.getAllUsers().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    public UserResponse getMe(Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        return new UserResponse(current);
    }

    @PutMapping("/me")
    public UserResponse updateMe(@Valid @RequestBody UserUpdateRequest req, Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        current.setUsername(req.getUsername());
        current.setEmail(req.getEmail());
        current.setBio(req.getBio());
        current.setProfilePhotoUrl(req.getProfilePhotoUrl());
        return new UserResponse(userService.updateUser(current.getId(), current));
    }

    @DeleteMapping("/me")
    public void deleteMe(Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        userService.deleteUser(current.getId());
    }
}
