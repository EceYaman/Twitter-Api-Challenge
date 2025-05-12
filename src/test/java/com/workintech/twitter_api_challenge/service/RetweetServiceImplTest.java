package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Retweet;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.RetweetNotFoundException;
import com.workintech.twitter_api_challenge.repository.RetweetRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetweetServiceImplTest {
    @Mock
    private RetweetRepository retweetRepo;
    @Mock
    private TweetRepository tweetRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private RetweetServiceImpl retweetService;

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
    @DisplayName("Bir kullanıcı bir tweet'i bir kez retweet edebiliyor mu?")
    void testRetweetSuccessFirstTime() {
        when(tweetRepo.findById(tweet.getId())).thenReturn(Optional.of(tweet));
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(retweetRepo.findByTweetIdAndUserId(tweet.getId(), user.getId()))
                .thenReturn(Optional.empty());
        when(retweetRepo.save(any(Retweet.class))).thenAnswer(inv -> {
            Retweet r = inv.getArgument(0);
            r.setId(7L);
            return r;
        });

        Retweet result = retweetService.retweet(tweet.getId(), user.getId());

        assertNotNull(result.getId());
        assertEquals(tweet, result.getTweet());
        assertEquals(user, result.getUser());
        assertNotNull(result.getCreationDate());
    }

    @Test
    @DisplayName("Var olan retweet silme işlemi başarılı mı?")
    void testDeleteRetweetSuccess() {
        when(retweetRepo.findByTweetIdAndUserId(tweet.getId(), user.getId()))
                .thenReturn(Optional.of(new Retweet()));

        assertDoesNotThrow(() -> retweetService.deleteRetweet(tweet.getId(), user.getId()));
        verify(retweetRepo).deleteByTweetIdAndUserId(tweet.getId(), user.getId());
    }

    @Test
    @DisplayName("Silmek istenen retweet bulunamayınca RetweetNotFoundException atılıyor mu?")
    void testDeleteRetweetNotFound() {
        when(retweetRepo.findByTweetIdAndUserId(tweet.getId(), user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(RetweetNotFoundException.class,
                () -> retweetService.deleteRetweet(tweet.getId(), user.getId()));
        verify(retweetRepo, never()).deleteByTweetIdAndUserId(anyLong(), anyLong());
    }
}
