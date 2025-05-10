package com.workintech.twitter_api_challenge.repository;

import com.workintech.twitter_api_challenge.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweetId(Long tweetId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND (c.user.id = :userId OR c.tweet.user.id = :userId)")
    void deleteByIdAndUserId(Long commentId, Long userId);
}
