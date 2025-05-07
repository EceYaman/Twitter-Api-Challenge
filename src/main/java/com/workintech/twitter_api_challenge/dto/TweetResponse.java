package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.Tweet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TweetResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Long userId;

    public TweetResponse(Tweet t) {
        this.id = t.getId();
        this.content = t.getContent();
        this.imageUrl = t.getImageUrl();
        this.creationDate = t.getCreationDate();
        this.updateDate = t.getUpdateDate();
        this.userId = t.getUser().getId();
    }
}
