package com.workintech.twitter_api_challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwitterErrorResponse {
    private String message;
    private Integer status;
    private long timestamp;
    private LocalDateTime localDateTime;
}
