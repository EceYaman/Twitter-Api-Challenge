package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.DislikeRequest;
import com.workintech.twitter_api_challenge.dto.LikeRequest;
import com.workintech.twitter_api_challenge.dto.LikeResponse;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.LikeService;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Slf4j
@RestController
@RequestMapping
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;

    @Autowired
    public LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    @PostMapping("/like")
    public LikeResponse like(
            @Valid @RequestBody LikeRequest req,
            Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        return new LikeResponse(
                likeService.likeTweet(req.getTweetId(), current.getId())
        );
    }

    @PostMapping("/dislike")
    public void dislike(
            @Valid @RequestBody DislikeRequest req,
            Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        likeService.dislikeTweet(req.getTweetId(), current.getId());
    }
}
