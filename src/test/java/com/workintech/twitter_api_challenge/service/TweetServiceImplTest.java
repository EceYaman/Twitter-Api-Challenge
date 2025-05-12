package com.workintech.twitter_api_challenge.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    private User mockUser;
    private Tweet tweetRequest;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        tweetRequest = new Tweet();
        tweetRequest.setContent("Test tweet içeriği");
    }

    @Test
    @DisplayName("Tweet başarıyla oluşturulabiliyor mu?")
    void testCreateTweetSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(tweetRepository.save(any(Tweet.class))).thenAnswer(invocation -> {
            Tweet t = invocation.getArgument(0);
            t.setId(100L);
            return t;
        });

        Tweet saved = tweetService.createTweet(1L, tweetRequest);

        assertNotNull(saved.getId(), "Kaydedilen tweet'in ID'si null olmamalı");
        assertEquals(mockUser, saved.getUser(), "Tweet sahibi doğru atanmalı");
        assertNotNull(saved.getCreationDate(), "Oluşturulma tarihi atanmalı");
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    @DisplayName("Kullanıcı bulunamadığında hata fırlatılıyor mu?")
    void testCreateTweetUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> tweetService.createTweet(2L, tweetRequest),
                "Kullanıcı bulunamazsa UserNotFoundException atılmalı");
        verify(tweetRepository, never()).save(any());
    }

    @Test
    @DisplayName("Tweet güncelleme sırasında tweet bulunamazsa hata fırlatılıyor mu?")
    void testUpdateTweetNotFound() {
        when(tweetRepository.findById(5L)).thenReturn(Optional.empty());

        Tweet update = new Tweet();
        update.setContent("Güncelleme testi");

        assertThrows(TweetNotFoundException.class,
                () -> tweetService.updateTweet(5L, update,1L),
                "Tweet bulunamazsa TweetNotFoundException atılmalı");
        verify(tweetRepository, never()).save(any());
    }
}
