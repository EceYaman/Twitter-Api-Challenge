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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommentResponse> create(
            @Valid @RequestBody CommentRequest req,
            Authentication auth
    ) {
        User user = userService.findByUsername(auth.getName());
        Comment entity = new Comment();
        entity.setContent(req.getContent());
        Comment saved = commentService.createComment(req.getTweetId(), user.getId(), entity);
        return ResponseEntity.status(201).body(new CommentResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest req,
            Authentication auth
    ) {
        Comment updated = commentService.updateComment(id, req.getContent(), auth.getName());
        return ResponseEntity.ok(new CommentResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth
    ) {
        commentService.deleteComment(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> byTweet(@RequestParam Long tweetId) {
        List<CommentResponse> list = commentService.getCommentsByTweet(tweetId)
                .stream()
                .map(comment -> {
                    CommentResponse response = new CommentResponse(comment);
                    response.setUserId(comment.getUser().getId());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
