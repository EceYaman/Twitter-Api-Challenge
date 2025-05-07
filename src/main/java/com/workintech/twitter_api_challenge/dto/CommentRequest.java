package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @NotNull
    private Long tweetId;

    @NotNull
    @NotEmpty(message = "Content is required")
    private String content;
}
