package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
}
