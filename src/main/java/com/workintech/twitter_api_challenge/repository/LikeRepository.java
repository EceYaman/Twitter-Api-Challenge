package com.workintech.twitter_api_challenge.repository;

import com.workintech.twitter_api_challenge.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTweetIdAndUserId(Long tweetId, Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.tweet.id = :tweetId AND l.user.id = :userId")
    void deleteByTweetIdAndUserId(Long tweetId, Long userId);
}
