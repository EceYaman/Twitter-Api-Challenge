package com.workintech.twitter_api_challenge.repository;

import com.workintech.twitter_api_challenge.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    Optional<Retweet> findByTweetIdAndUserId(Long tweetId, Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Retweet r WHERE r.tweet.id = :tweetId AND r.user.id = :userId")
    void deleteByTweetIdAndUserId(Long tweetId, Long userId);
}
