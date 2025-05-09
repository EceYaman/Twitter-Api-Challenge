package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.DislikeRequest;
import com.workintech.twitter_api_challenge.dto.LikeRequest;
import com.workintech.twitter_api_challenge.dto.LikeResponse;
import com.workintech.twitter_api_challenge.entity.Like;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.LikeService;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Slf4j
@RestController
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @Autowired
    public LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    @PostMapping("/like")
    public ResponseEntity<LikeResponse> like(
            @Valid @RequestBody LikeRequest req,
            Authentication auth
    ) {
        User user = userService.findByUsername(auth.getName());
        Like saved = likeService.likeTweet(req.getTweetId(), user.getId());
        return ResponseEntity.status(201).body(new LikeResponse(saved));
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(
            @Valid @RequestBody DislikeRequest req,
            Authentication auth
    ) {
        User user = userService.findByUsername(auth.getName());
        likeService.dislikeTweet(req.getTweetId(), user.getId());
        return ResponseEntity.noContent().build();
    }
}
