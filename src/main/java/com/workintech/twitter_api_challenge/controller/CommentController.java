package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.CommentRequest;
import com.workintech.twitter_api_challenge.dto.CommentResponse;
import com.workintech.twitter_api_challenge.entity.Comment;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.service.CommentService;
import com.workintech.twitter_api_challenge.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    public CommentResponse create(
            @Valid @RequestBody CommentRequest req,
            Authentication authentication) {
        String username = authentication.getName();
        User current = userService.findByUsername(username);
        Comment c = new Comment();
        c.setContent(req.getContent());
        Comment saved = commentService.createComment(req.getTweetId(), current.getId(), c);
        return new CommentResponse(saved);
    }

    @PutMapping("/{id}")
    public CommentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest req,
            Authentication authentication) {
        String username = authentication.getName();
        Comment updated = commentService.updateComment(
                id,
                req.getContent(),
                username
        );
        return new CommentResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        commentService.deleteComment(id, username);
    }

    @GetMapping
    public List<CommentResponse> byTweet(@RequestParam Long tweetId) {
        return commentService.getCommentsByTweet(tweetId).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
}
