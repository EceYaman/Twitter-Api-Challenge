package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Retweet;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;

import java.util.List;

public interface RetweetService {
    Retweet retweet(Long tweetId, Long userId);
    void deleteRetweet(Long tweetId, Long userId);
    void deleteRetweetById(Long id);
}
