package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Retweet;

import java.time.LocalDateTime;

public class RetweetResponse {
    private Long id;
    private LocalDateTime creationDate;
    private Long tweetId;
    private Long userId;
    public RetweetResponse(Retweet r) {
        this.id = r.getId();
        this.creationDate = r.getCreationDate();
        this.tweetId = r.getTweet().getId();
        this.userId = r.getUser().getId();
    }

    public Long getId() { return id; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Long getTweetId() { return tweetId; }
    public Long getUserId() { return userId; }
}
