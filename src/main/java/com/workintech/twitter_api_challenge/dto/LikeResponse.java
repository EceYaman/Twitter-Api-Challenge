package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Like;

import java.time.LocalDateTime;

public class LikeResponse {
    private Long id;
    private LocalDateTime creationDate;
    private Long tweetId;
    private Long userId;
    public LikeResponse(Like l) {
        this.id = l.getId();
        this.creationDate = l.getCreationDate();
        this.tweetId = l.getTweet().getId();
        this.userId = l.getUser().getId();
    }

    public Long getId() { return id; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Long getTweetId() { return tweetId; }
    public Long getUserId() { return userId; }
}
