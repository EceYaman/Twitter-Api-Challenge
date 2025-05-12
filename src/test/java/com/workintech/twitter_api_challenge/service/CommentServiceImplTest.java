package com.workintech.twitter_api_challenge.service;
import com.workintech.twitter_api_challenge.entity.Comment;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.CommentNotFoundException;
import com.workintech.twitter_api_challenge.repository.CommentRepository;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepo;

    @Mock
    private TweetRepository tweetRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Tweet sampleTweet;
    private User sampleUser;
    private Comment sampleComment;

    @BeforeEach
    void init() {
        sampleTweet = new Tweet();
        sampleTweet.setId(10L);
        sampleUser = new User();
        sampleUser.setId(20L);
        sampleComment = new Comment();
        sampleComment.setId(200L);
        sampleComment.setTweet(sampleTweet);
        sampleComment.setUser(sampleUser);
    }

    @Test
    @DisplayName("Yorum başarıyla oluşturulabiliyor mu?")
    void testCreateCommentSuccess() {
        when(tweetRepo.findById(10L)).thenReturn(Optional.of(sampleTweet));
        when(userRepo.findById(20L)).thenReturn(Optional.of(sampleUser));
        when(commentRepo.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment c = invocation.getArgument(0);
            c.setId(300L);
            return c;
        });

        Comment request = new Comment();
        request.setContent("Harika tweet!");

        Comment result = commentService.createComment(10L, 20L, request);

        assertNotNull(result.getId(), "Kaydedilen yorumun ID'si olmalı");
        assertEquals(sampleTweet, result.getTweet(), "Yorum doğru tweete ait olmalı");
        assertEquals(sampleUser, result.getUser(), "Yorum sahibi doğru atanmalı");
        assertNotNull(result.getCreationDate(), "Oluşturulma tarihi atanmalı");
    }

    @Test
    @DisplayName("Yorumu güncellerken yetkisiz kullanıcıda fırlatılıyor mu?")
    void testUpdateCommentUnauthorized() {
        User owner = new User(); owner.setUsername("sahip");
        Tweet tweet = new Tweet(); tweet.setUser(new User());
        tweet.getUser().setUsername("sahip");
        sampleComment.setUser(owner);
        sampleComment.setTweet(tweet);

        when(commentRepo.findById(200L)).thenReturn(Optional.of(sampleComment));

        assertThrows(SecurityException.class,
                () -> commentService.updateComment(200L, "İzinsiz güncelleme", "yetkisiz"),
                "Yetkisiz kullanıcı SecurityException atmalı");
        verify(commentRepo, never()).save(any());
    }

    @Test
    @DisplayName("Yorum silme sırasında yorumu bulunamazsa hata fırlatıyor mu?")
    void testDeleteCommentNotFound() {
        when(commentRepo.findById(400L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.deleteComment(400L, 50L),
                "Bulunamayan yorum için CommentNotFoundException atılmalı");
        verify(commentRepo, never()).deleteByIdAndUserId(any(), any());
    }
}