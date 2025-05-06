package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Long tweetId;
    private Long userId;
    public CommentResponse(Comment c) {
        this.id = c.getId();
        this.content = c.getContent();
        this.creationDate = c.getCreationDate();
        this.updateDate = c.getUpdateDate();
        this.tweetId = c.getTweet().getId();
        this.userId = c.getUser().getId();
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public LocalDateTime getUpdateDate() { return updateDate; }
    public Long getTweetId() { return tweetId; }
    public Long getUserId() { return userId; }
}
