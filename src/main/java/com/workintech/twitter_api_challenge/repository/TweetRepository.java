package com.workintech.twitter_api_challenge.repository;

import com.workintech.twitter_api_challenge.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Tweet t WHERE t.id = :tweetId AND t.user.id = :userId")
    void deleteByIdAndUserId(Long tweetId, Long userId);
}
