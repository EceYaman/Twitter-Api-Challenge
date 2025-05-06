package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.NotNull;

public class RetweetRequest {
    @NotNull
    private Long tweetId;
    @NotNull
    private Long userId;

    public Long getTweetId() { return tweetId; }
    public void setTweetId(Long tweetId) { this.tweetId = tweetId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
