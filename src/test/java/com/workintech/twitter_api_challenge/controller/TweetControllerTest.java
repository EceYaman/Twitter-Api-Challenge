package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.TweetRequest;
import com.workintech.twitter_api_challenge.dto.TweetResponse;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TwitterException;
import com.workintech.twitter_api_challenge.service.TweetService;
import com.workintech.twitter_api_challenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TweetControllerTest {

    @Mock
    private TweetService tweetService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TweetController tweetController;

    private Authentication auth;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auth = new UsernamePasswordAuthenticationToken("testuser", null);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
    }

    @Test
    @DisplayName("Tweet oluşturma isteği başarılı olduğunda 201 dönüyor mu?")
    void testCreateTweetReturnsCreatedAndCorrectBody() {
        when(userService.findByUsername(eq("testuser"))).thenReturn(testUser);

        TweetRequest req = new TweetRequest();
        req.setContent("Yeni tweet içeriği");
        req.setImageUrl(null);

        Tweet saved = new Tweet();
        saved.setId(100L);
        saved.setContent(req.getContent());
        saved.setCreationDate(LocalDateTime.now());
        saved.setUser(testUser);

        when(tweetService.createTweet(eq(1L), any(Tweet.class))).thenReturn(saved);

        ResponseEntity<TweetResponse> response = tweetController.create(req, auth);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(100L);
        assertThat(response.getBody().getContent()).isEqualTo("Yeni tweet içeriği");
        verify(tweetService).createTweet(eq(1L), any(Tweet.class));
    }

    @Test
    @DisplayName("Tweet oluşturma sırasında kullanıcı bulunamazsa NotFound hatası dönüyor mu?")
    void testCreateTweetWhenUserNotFoundThrowsException() {
        when(userService.findByUsername(anyString()))
                .thenThrow(new TwitterException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));

        TweetRequest req = new TweetRequest();
        req.setContent("İçerik");

        assertThatThrownBy(() -> tweetController.create(req, auth))
                .isInstanceOf(TwitterException.class)
                .hasMessageContaining("Kullanıcı bulunamadı");
    }

    @Test
    @DisplayName("Kullanıcıya ait tweetler listelenirken doğru sayıda TweetResponse dönülüyor mu?")
    void testFindByUserReturnsListOfTweetResponses() {
        when(userService.findByUsername(eq("testuser"))).thenReturn(testUser);

        Tweet t1 = new Tweet(); t1.setId(1L); t1.setContent("A"); t1.setUser(testUser);
        Tweet t2 = new Tweet(); t2.setId(2L); t2.setContent("B"); t2.setUser(testUser);
        when(tweetService.getTweetsByUser(eq(1L))).thenReturn(List.of(t1, t2));

        ResponseEntity<List<TweetResponse>> response = tweetController.byUser(auth);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        verify(tweetService).getTweetsByUser(eq(1L));
    }

    @Test
    @DisplayName("Tweet listelenirken servis hatası olursa exception fırlatılıyor mu?")
    void testFindByUserWhenServiceThrowsException() {
        when(userService.findByUsername(anyString()))
                .thenReturn(testUser);
        when(tweetService.getTweetsByUser(anyLong()))
                .thenThrow(new RuntimeException("DB hatası"));

        assertThatThrownBy(() -> tweetController.byUser(auth))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB hatası");
    }
}

