package com.workintech.twitter_api_challenge.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TweetServiceImplTest {
    @Mock
    private TweetRepository tweetRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private TweetServiceImpl tweetService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        user = new User(); user.setId(1L);
        tweet = new Tweet(); tweet.setId(2L); tweet.setUser(user);
    }

    @Test
    @DisplayName("Tweet başarıyla oluşturulabiliyor mu?")
    void testCreateTweetSuccess() {
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(tweetRepo.save(any(Tweet.class))).thenAnswer(inv -> {
            Tweet t = inv.getArgument(0);
            t.setId(100L);
            return t;
        });

        Tweet saved = tweetService.createTweet(user.getId(), tweet);

        assertNotNull(saved.getId());
        assertEquals(user, saved.getUser());
        assertNotNull(saved.getCreationDate());
        verify(tweetRepo).save(any(Tweet.class));
    }

    @Test
    @DisplayName("Kullanıcı bulunamadığında UserNotFoundException fırlatılıyor mu?")
    void testCreateTweetUserNotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> tweetService.createTweet(99L, tweet));
        verify(tweetRepo, never()).save(any());
    }

    @Test
    @DisplayName("Belirli kullanıcıya ait tweetler listelenebiliyor mu?")
    void testGetTweetsByUser() {
        when(tweetRepo.findByUserId(user.getId())).thenReturn(List.of(tweet));
        List<Tweet> list = tweetService.getTweetsByUser(user.getId());
        assertEquals(1, list.size());
        assertEquals(tweet, list.get(0));
    }

    @Test
    @DisplayName("Var olan tweetId ile doğru Tweet getiriliyor mu?")
    void testGetTweetByIdSuccess() {
        when(tweetRepo.findById(tweet.getId())).thenReturn(Optional.of(tweet));
        Tweet result = tweetService.getTweetById(tweet.getId());
        assertEquals(tweet, result);
    }

    @Test
    @DisplayName("TweetId bulunamayınca TweetNotFoundException fırlatılıyor mu?")
    void testGetTweetByIdNotFound() {
        when(tweetRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class,
                () -> tweetService.getTweetById(99L));
    }

    @Test
    @DisplayName("Mevcut tweet başarıyla güncellenebiliyor mu?")
    void testUpdateTweetSuccess() {
        Tweet updated = new Tweet(); updated.setContent("Yeni içerik");
        when(tweetRepo.findById(tweet.getId())).thenReturn(Optional.of(tweet));
        when(tweetRepo.save(any(Tweet.class))).thenAnswer(inv -> inv.getArgument(0));
        Tweet result = tweetService.updateTweet(tweet.getId(), updated, user.getId());
        assertEquals("Yeni içerik", result.getContent());
        assertNotNull(result.getUpdateDate());
    }

    @Test
    @DisplayName("Tweet güncelleme sırasında tweet bulunamazsa TweetNotFoundException fırlatılıyor mu?")
    void testUpdateTweetNotFound() {
        when(tweetRepo.findById(5L)).thenReturn(Optional.empty());
        Tweet update = new Tweet(); update.setContent("Deneme");
        assertThrows(TweetNotFoundException.class,
                () -> tweetService.updateTweet(5L, update, user.getId()));
        verify(tweetRepo, never()).save(any());
    }

    @Test
    @DisplayName("Tweet sahibi kendi tweetini silebiliyor mu?")
    void testDeleteTweetSuccess() {
        when(tweetRepo.findById(tweet.getId())).thenReturn(Optional.of(tweet));
        assertDoesNotThrow(() -> tweetService.deleteTweet(tweet.getId(), user.getId()));
        verify(tweetRepo).deleteByIdAndUserId(tweet.getId(), user.getId());
    }

    @Test
    @DisplayName("Farklı userId ile deleteTweet çağrısında SecurityException fırlatılıyor mu?")
    void testDeleteTweetSecurityException() {
        when(tweetRepo.findById(tweet.getId())).thenReturn(Optional.of(tweet));

        assertThrows(SecurityException.class,
                () -> tweetService.deleteTweet(tweet.getId(), 999L));
    }
}

