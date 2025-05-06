package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Tweet;

import java.util.List;

public interface TweetService {
    Tweet createTweet(Long userId, Tweet tweet);
    List<Tweet> getTweetsByUser(Long userId);
    Tweet getTweetById(Long id);
    Tweet updateTweet(Long id, Tweet tweet);
    void deleteTweet(Long id, String username);
}
