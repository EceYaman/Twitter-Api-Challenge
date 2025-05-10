package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Retweet;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.RetweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.TweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.RetweetRepository;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class RetweetServiceImpl implements RetweetService{
    private final RetweetRepository retweetRepo;
    private final TweetRepository tweetRepo;
    private final UserRepository userRepo;

    @Autowired
    public RetweetServiceImpl(RetweetRepository retweetRepo, TweetRepository tweetRepo, UserRepository userRepo) {
        this.retweetRepo = retweetRepo;
        this.tweetRepo = tweetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Retweet retweet(Long tweetId, Long userId) {
        Tweet tweet = tweetRepo.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı (id: " + tweetId + ")"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı (id: " + userId + ")"));
        return retweetRepo.findByTweetIdAndUserId(tweetId, userId)
                .orElseGet(() -> {
                    Retweet rt = new Retweet();
                    rt.setTweet(tweet);
                    rt.setUser(user);
                    rt.setCreationDate(LocalDateTime.now());
                    return retweetRepo.save(rt);
                });
    }

    @Override
    public void deleteRetweet(Long tweetId, Long userId) {
        retweetRepo.findByTweetIdAndUserId(tweetId, userId)
                .orElseThrow(() -> new RetweetNotFoundException(
                        "Retweet bulunamadı (tweet: " + tweetId + ", user: " + userId + ")"));

        retweetRepo.deleteByTweetIdAndUserId(tweetId, userId);
    }

}
