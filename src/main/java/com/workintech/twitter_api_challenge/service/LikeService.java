package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Like;


public interface LikeService {
    Like likeTweet(Long tweetId, Long userId);
    void dislikeTweet(Long tweetId, Long userId);
}
