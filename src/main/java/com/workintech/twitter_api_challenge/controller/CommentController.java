package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.CommentRequest;
import com.workintech.twitter_api_challenge.dto.CommentResponse;
import com.workintech.twitter_api_challenge.entity.Comment;
import com.workintech.twitter_api_challenge.service.CommentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponse create(@Valid @RequestBody CommentRequest req){
        Comment c = new Comment();
        c.setContent(req.getContent());
        Comment saved = commentService.createComment(
                req.getTweetId(), req.getUserId(), c
        );
        return new CommentResponse(saved);
    }

    @PutMapping("/{id}")
    public CommentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest req){
        Comment updated = commentService.updateComment(
                id,
                req.getContent(),
                req.getUserId().toString()
        );
        return new CommentResponse(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam Long userId){
        commentService.deleteComment(id, userId.toString());
    }

    @GetMapping
    public List<CommentResponse> byTweet(@RequestParam Long tweetId){
        return commentService.getCommentsByTweet(tweetId).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
}
