package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Retweet;


public interface RetweetService {
    Retweet retweet(Long tweetId, Long userId);
    void deleteRetweet(Long tweetId, Long userId);
}
