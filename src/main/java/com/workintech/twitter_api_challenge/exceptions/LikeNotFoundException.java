package com.workintech.twitter_api_challenge.exceptions;

import org.springframework.http.HttpStatus;

public class LikeNotFoundException extends TwitterException {

    public LikeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
