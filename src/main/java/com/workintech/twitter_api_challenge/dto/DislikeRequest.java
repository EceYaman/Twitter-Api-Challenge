package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DislikeRequest {
    @NotNull
    private Long tweetId;
}
