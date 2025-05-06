package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.entity.Comment;
import com.workintech.twitter_api_challenge.entity.Like;
import com.workintech.twitter_api_challenge.entity.Tweet;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.LikeNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.TweetNotFoundException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.LikeRepository;
import com.workintech.twitter_api_challenge.repository.TweetRepository;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepo;
    private final TweetRepository tweetRepo;
    private final UserRepository userRepo;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepo, TweetRepository tweetRepo, UserRepository userRepo) {
        this.likeRepo = likeRepo;
        this.tweetRepo = tweetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Like likeTweet(Long tweetId, Long userId) {
        Tweet tweet = tweetRepo.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı (id: " + tweetId + ")"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı (id: " + userId + ")"));
        return likeRepo.findByTweetIdAndUserId(tweetId, userId)
                .orElseGet(() -> {
                    Like like = new Like();
                    like.setTweet(tweet);
                    like.setUser(user);
                    like.setCreationDate(LocalDateTime.now());
                    return likeRepo.save(like);
                });
    }

    @Override
    public void dislikeTweet(Long tweetId, Long userId) {
        likeRepo.findByTweetIdAndUserId(tweetId, userId)
                .orElseThrow(() -> new LikeNotFoundException("Like bulunamadı (tweet: " + tweetId + ", user: " + userId + ")"));
        likeRepo.deleteByTweetIdAndUserId(tweetId, userId);
    }
}
