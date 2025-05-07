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
    public RetweetResponse retweet(
            @Valid @RequestBody RetweetRequest req,
            Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        return new RetweetResponse(
                retweetService.retweet(req.getTweetId(), current.getId())
        );
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Authentication authentication) {
        retweetService.deleteRetweetById(id);
    }
}
