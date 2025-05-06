package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Comment;
import com.workintech.twitter_api_challenge.entity.Like;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;

import java.util.List;

public interface LikeService {
    Like likeTweet(Long tweetId, Long userId);
    void dislikeTweet(Long tweetId, Long userId);
}
