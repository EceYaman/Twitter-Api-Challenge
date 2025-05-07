package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.UserResponse;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody User user) {
        return new UserResponse(userService.createUser(user));
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest login) {
        User u = userService.findByUsername(login.getUsername());
        if (!u.getPassword().equals(login.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return new UserResponse(u);
    }
}