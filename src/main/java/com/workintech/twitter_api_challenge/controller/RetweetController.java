package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.RetweetRequest;
import com.workintech.twitter_api_challenge.dto.RetweetResponse;
import com.workintech.twitter_api_challenge.entity.Retweet;
import com.workintech.twitter_api_challenge.service.RetweetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/retweet")
public class RetweetController {
    private final RetweetService retweetService;

    @Autowired
    public RetweetController(RetweetService retweetService) {
        this.retweetService = retweetService;
    }

    @PostMapping
    public RetweetResponse retweet(@Valid @RequestBody RetweetRequest req) {
        return new RetweetResponse(
                retweetService.retweet(req.getTweetId(), req.getUserId())
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        retweetService.deleteRetweetById(id);
    }
}
