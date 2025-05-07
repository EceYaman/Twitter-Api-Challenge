package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.TweetRequest;
import com.workintech.twitter_api_challenge.dto.TweetResponse;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.TweetService;
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
    public TweetResponse create(
            @Valid @RequestBody TweetRequest req,
            Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        Tweet entity = new Tweet();
        entity.setContent(req.getContent());
        entity.setImageUrl(req.getImageUrl());
        Tweet saved = tweetService.createTweet(current.getId(), entity);
        return new TweetResponse(saved);
    }

    @GetMapping("/findByUserId")
    public List<TweetResponse> byUser(Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        return tweetService.getTweetsByUser(current.getId()).stream()
                .map(TweetResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/findById")
    public TweetResponse byId(@RequestParam Long id) {
        return new TweetResponse(tweetService.getTweetById(id));
    }

    @PutMapping("/{id}")
    public TweetResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TweetRequest req,
            Authentication authentication) {
        Tweet entity = new Tweet();
        entity.setContent(req.getContent());
        entity.setImageUrl(req.getImageUrl());
        Tweet updated = tweetService.updateTweet(id, entity);
        return new TweetResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        tweetService.deleteTweet(id, username);
    }
}
