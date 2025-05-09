package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Comment;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.CommentNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.TweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.CommentRepository;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepo;
    private final TweetRepository tweetRepo;
    private final UserRepository userRepo;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepo, TweetRepository tweetRepo, UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.tweetRepo = tweetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Comment createComment(Long tweetId, Long userId, Comment comment) {
        Tweet tweet = tweetRepo.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı (id: " + tweetId + ")"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı (id: " + userId + ")"));
        comment.setTweet(tweet);
        comment.setUser(user);
        comment.setCreationDate(LocalDateTime.now());
        return commentRepo.save(comment);
    }

    @Override
    public Comment updateComment(Long id, String content, String username) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Yorum bulunamadı (id: " + id + ")"));
        if (!comment.getUser().getUsername().equals(username) && !comment.getTweet().getUser().getUsername().equals(username)) {
            throw new SecurityException("Bu yorumu güncelleme yetkiniz yok");
        }
        comment.setContent(content);
        comment.setUpdateDate(LocalDateTime.now());
        return commentRepo.save(comment);
    }

    @Override
    public void deleteComment(Long id, String username) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Yorum bulunamadı (id: " + id + ")"));
        if (!comment.getUser().getUsername().equals(username) && !comment.getTweet().getUser().getUsername().equals(username)) {
            throw new SecurityException("Bu yorumu silme yetkiniz yok");
        }
        commentRepo.delete(comment);
    }

    @Override
    public List<Comment> getCommentsByTweet(Long tweetId) {
        return commentRepo.findByTweetId(tweetId);
    }
}
