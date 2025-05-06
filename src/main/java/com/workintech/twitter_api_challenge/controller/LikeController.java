package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.DislikeRequest;
import com.workintech.twitter_api_challenge.dto.LikeRequest;
import com.workintech.twitter_api_challenge.dto.LikeResponse;
import com.workintech.twitter_api_challenge.service.LikeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public LikeResponse like(@Valid @RequestBody LikeRequest req) {
        return new LikeResponse(
                likeService.likeTweet(req.getTweetId(), req.getUserId())
        );
    }

    @PostMapping("/dislike")
    public void dislike(@Valid @RequestBody DislikeRequest req) {
        likeService.dislikeTweet(req.getTweetId(), req.getUserId());
    }
}
