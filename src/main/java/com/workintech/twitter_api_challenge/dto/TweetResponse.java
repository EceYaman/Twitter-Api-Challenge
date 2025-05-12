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
    private String username;
    private String profilePhotoUrl;
    private int commentCount;
    private int likeCount;
    private int retweetCount;

    public TweetResponse(Tweet t) {
        this.id = t.getId();
        this.content = t.getContent();
        this.imageUrl = t.getImageUrl();
        this.creationDate = t.getCreationDate();
        this.updateDate = t.getUpdateDate();
        this.userId = t.getUser().getId();
        this.username = t.getUser().getUsername();
        this.profilePhotoUrl = t.getUser().getProfilePhotoUrl();
        this.commentCount = t.getComments() != null ? t.getComments().size() : 0;
        this.likeCount    = t.getLikes()    != null ? t.getLikes().size()    : 0;
        this.retweetCount = t.getRetweets() != null ? t.getRetweets().size() : 0;
    }
}
