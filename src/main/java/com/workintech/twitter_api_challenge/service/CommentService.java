package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long tweetId, Long userId, Comment comment);
    Comment updateComment(Long id, String content, String username);
    void deleteComment(Long id, Long userId);
    List<Comment> getCommentsByTweet(Long tweetId);
}
