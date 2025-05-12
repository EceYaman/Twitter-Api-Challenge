package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Like;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.LikeNotFoundException;
import com.workintech.twitter_api_challenge.repository.LikeRepository;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepo;
    @Mock
    private TweetRepository tweetRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private LikeServiceImpl likeService;

    private Tweet tweet;
    private User user;

    @BeforeEach
    void setUp() {
        tweet = new Tweet();
        tweet.setId(1L);
        user = new User();
        user.setId(2L);
    }

    @Test
    @DisplayName("Bir kullanıcı bir tweet'e bir kez like atabiliyor mu?")
    void testLikeTweetSuccessFirstTime() {
        when(tweetRepo.findById(tweet.getId())).thenReturn(Optional.of(tweet));
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(likeRepo.findByTweetIdAndUserId(tweet.getId(), user.getId()))
                .thenReturn(Optional.empty());
        when(likeRepo.save(any(Like.class))).thenAnswer(inv -> {
            Like l = inv.getArgument(0);
            l.setId(5L);
            return l;
        });

        Like result = likeService.likeTweet(tweet.getId(), user.getId());

        assertNotNull(result.getId());
        assertEquals(tweet, result.getTweet());
        assertEquals(user, result.getUser());
        assertNotNull(result.getCreationDate());
        verify(likeRepo).save(any(Like.class));
    }

    @Test
    @DisplayName("Dislike ile var olan like silinebiliyor mu?")
    void testDislikeTweetSuccess() {
        when(likeRepo.findByTweetIdAndUserId(tweet.getId(), user.getId()))
                .thenReturn(Optional.of(new Like()));

        assertDoesNotThrow(() -> likeService.dislikeTweet(tweet.getId(), user.getId()));
        verify(likeRepo).deleteByTweetIdAndUserId(tweet.getId(), user.getId());
    }

    @Test
    @DisplayName("Silmek istenen like bulunamayınca LikeNotFoundException fırlatılıyor mu?")
    void testDislikeTweetNotFound() {
        when(likeRepo.findByTweetIdAndUserId(tweet.getId(), user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(LikeNotFoundException.class,
                () -> likeService.dislikeTweet(tweet.getId(), user.getId()));
        verify(likeRepo, never()).deleteByTweetIdAndUserId(anyLong(), anyLong());
    }
}