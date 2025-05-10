package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.TweetRequest;
import com.workintech.twitter_api_challenge.dto.TweetResponse;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.TweetService;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.transaction.Transactional;
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
@RequestMapping("/tweet")
public class TweetController {
    private final TweetService tweetService;
    private final UserService userService;

    @Autowired
    public TweetController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TweetResponse> create(
            @Valid @RequestBody TweetRequest req,
            Authentication auth
    ) {
        User user = userService.findByUsername(auth.getName());
        Tweet entity = new Tweet();
        entity.setContent(req.getContent());
        entity.setImageUrl(req.getImageUrl());
        entity.setUser(user);
        Tweet saved = tweetService.createTweet(user.getId(), entity);
        return ResponseEntity.status(201).body(new TweetResponse(saved));
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetResponse>> byUser(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        List<TweetResponse> list = tweetService.getTweetsByUser(user.getId())
                .stream()
                .map(TweetResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/findById")
    public ResponseEntity<TweetResponse> byId(@RequestParam Long id) {
        Tweet t = tweetService.getTweetById(id);
        return ResponseEntity.ok(new TweetResponse(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TweetRequest req,
            Authentication auth
    ) {
        User user = userService.findByUsername(auth.getName());
        Tweet entity = new Tweet();
        entity.setContent(req.getContent());
        entity.setImageUrl(req.getImageUrl());
        Tweet updated = tweetService.updateTweet(id, entity, user.getId());
        return ResponseEntity.ok(new TweetResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth
    ) {
        Long userId = userService.findByUsername(auth.getName()).getId();
        tweetService.deleteTweet(id, userId);
        return ResponseEntity.noContent().build();
    }
}
