package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.TweetRequest;
import com.workintech.twitter_api_challenge.dto.TweetResponse;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.service.TweetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/tweet")
public class TweetController {
    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public TweetResponse create(@Valid @RequestBody TweetRequest req){
        Tweet entity = new Tweet();
        entity.setContent(req.getContent());
        entity.setImageUrl(req.getImageUrl());
        Tweet saved = tweetService.createTweet(req.getUserId(), entity);
        return new TweetResponse(saved);
    }

    @GetMapping("/findByUserId")
    public List<TweetResponse> byUser(@RequestParam Long userId){
        return tweetService.getTweetsByUser(userId).stream()
                .map(TweetResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/findById")
    public TweetResponse byId(@RequestParam Long id){
        return new TweetResponse(tweetService.getTweetById(id));
    }

    @PutMapping("/{id}")
    public TweetResponse update(@PathVariable Long id, @Valid @RequestBody TweetRequest req){
        Tweet entity = new Tweet();
        entity.setContent(req.getContent());
        entity.setImageUrl(req.getImageUrl());
        Tweet updated = tweetService.updateTweet(id, entity);
        return new TweetResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        tweetService.deleteTweet(id, null);
    }
}
