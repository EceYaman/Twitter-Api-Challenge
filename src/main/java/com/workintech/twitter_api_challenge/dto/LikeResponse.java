package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
}
