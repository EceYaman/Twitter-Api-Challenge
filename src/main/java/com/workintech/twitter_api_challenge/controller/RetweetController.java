package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.RetweetRequest;
import com.workintech.twitter_api_challenge.dto.RetweetResponse;
import com.workintech.twitter_api_challenge.entity.Retweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.RetweetService;
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
@RequestMapping("/retweet")
public class RetweetController {
    private final RetweetService retweetService;
    private final UserService userService;

    @Autowired
    public RetweetController(RetweetService retweetService, UserService userService) {
        this.retweetService = retweetService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<RetweetResponse> retweet(
            @Valid @RequestBody RetweetRequest req,
            Authentication auth
    ) {
        User user = userService.findByUsername(auth.getName());
        Retweet saved = retweetService.retweet(req.getTweetId(), user.getId());
        return ResponseEntity.status(201).body(new RetweetResponse(saved));
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> deleteRetweet(
            @PathVariable Long tweetId,
            Authentication auth
    ) {
        Long userId = userService.findByUsername(auth.getName()).getId();
        retweetService.deleteRetweet(tweetId, userId);
        return ResponseEntity.noContent().build();
    }
}
