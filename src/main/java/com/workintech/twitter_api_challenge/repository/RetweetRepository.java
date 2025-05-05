package com.workintech.twitter_api_challenge.repository;

import com.workintech.twitter_api_challenge.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    Optional<Retweet> findByTweetIdAndUserId(Long tweetId, Long userId);
    void deleteByTweetIdAndUserId(Long tweetId, Long userId);
}
