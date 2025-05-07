package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TweetRequest {
    @NotNull(message = "Content is required")
    @NotEmpty(message = "Content cannot be empty")
    private String content;

    private String imageUrl;
}
