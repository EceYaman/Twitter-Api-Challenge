package com.workintech.twitter_api_challenge.repository;

import com.workintech.twitter_api_challenge.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTweetIdAndUserId(Long tweetId, Long userId);
    void deleteByTweetIdAndUserId(Long tweetId, Long userId);
}
