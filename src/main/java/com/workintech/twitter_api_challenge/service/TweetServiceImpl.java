package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Transactional
@Service
public class TweetServiceImpl implements TweetService{
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Tweet createTweet(Long userId, Tweet tweet) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı (id: " + userId + ")"));
        tweet.setUser(user);
        tweet.setCreationDate(LocalDateTime.now());
        return tweetRepository.save(tweet);
    }

    @Override
    public List<Tweet> getTweetsByUser(Long userId) {
        return tweetRepository.findByUserId(userId);
    }

    @Override
    public Tweet getTweetById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı (id: " + id + ")"));
    }

    @Override
    public Tweet updateTweet(Long id, Tweet tweetDetails, Long userId) {
        Tweet tweet = getTweetById(id);
        if (!tweet.getUser().getId().equals(userId)) {
            throw new SecurityException("Bu tweeti güncelleme yetkiniz yok");
        }
        tweet.setContent(tweetDetails.getContent());
        tweet.setImageUrl(tweetDetails.getImageUrl());
        tweet.setUpdateDate(LocalDateTime.now());
        return tweetRepository.save(tweet);
    }

    @Override
    public void deleteTweet(Long id, Long userId) {
        Tweet tweet = getTweetById(id);
        if (!tweet.getUser().getId().equals(userId)) {
            throw new SecurityException("Bu tweeti silme yetkiniz yok");
        }
        tweetRepository.deleteByIdAndUserId(id, userId);
    }
}