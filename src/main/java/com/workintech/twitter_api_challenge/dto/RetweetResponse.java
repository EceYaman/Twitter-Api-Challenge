package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Retweet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
}
