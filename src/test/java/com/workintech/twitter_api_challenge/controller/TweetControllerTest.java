package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import com.workintech.twitter_api_challenge.service.TweetServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TweetServiceImplTest {

    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TweetServiceImpl tweetService;

    private User user;
    private Tweet tweet;

    @BeforeEach
    void setUp() {
        user = new User(); user.setId(1L);
        tweet = new Tweet();
        tweet.setId(100L);
        tweet.setContent("Test");
        tweet.setCreationDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Tweet başarılı şekilde oluşturulabiliyor mu?")
    void testCreateTweetSuccess() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(tweetRepository.save(any(Tweet.class))).willAnswer(invocation -> {
            Tweet t = invocation.getArgument(0);
            t.setId(tweet.getId());
            t.setCreationDate(LocalDateTime.now());
            return t;
        });

        Tweet result = tweetService.createTweet(1L, new Tweet());
        assertEquals(100L, result.getId());
        assertNotNull(result.getCreationDate());
    }

    @Test
    @DisplayName("Kullanıcı bulunamazsa hata fırlatılıyor mu?")
    void testCreateTweetUserNotFound() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> tweetService.createTweet(1L, new Tweet()));
    }

    @Test
    @DisplayName("Tweet id ile başarılı şekilde bulunabiliyor mu?")
    void testGetTweetByIdSuccess() {
        given(tweetRepository.findById(100L)).willReturn(Optional.of(tweet));
        Tweet found = tweetService.getTweetById(100L);
        assertEquals(100L, found.getId());
    }

    @Test
    @DisplayName("Geçersiz id ile tweet bulunamayınca hata fırlatılıyor mu?")
    void testGetTweetByIdNotFound() {
        given(tweetRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(TweetNotFoundException.class, () -> tweetService.getTweetById(1L));
    }

    @Test
    @DisplayName("Tweet sahibi başarılı şekilde tweet silebiliyor mu?")
    void testDeleteTweetSuccess() {
        tweet.setUser(user);
        given(tweetRepository.findById(100L)).willReturn(Optional.of(tweet));
        assertDoesNotThrow(() -> tweetService.deleteTweet(100L, 1L));
    }

    @Test
    @DisplayName("Sahibi olmayan kullanıcı tweet silmeye çalışınca hata fırlatılıyor mu?")
    void testDeleteTweetSecurityException() {
        User other = new User(); other.setId(2L);
        tweet.setUser(user);
        given(tweetRepository.findById(100L)).willReturn(Optional.of(tweet));
        assertThrows(SecurityException.class, () -> tweetService.deleteTweet(100L, 2L));
    }
}





