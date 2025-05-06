package com.workintech.twitter_api_challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "twitter_api")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(min=3,max = 100)
    @Column(name = "username")
    private String username;

    @NotNull
    @NotEmpty
    @Size(min=5,max = 150)
    @Column(name = "email")
    private String email;

    @NotNull
    @NotEmpty
    @Size(min=8,max = 100)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Tweet> tweets = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Retweet> retweets = new HashSet<>();

    public void addTweet(Tweet tweet){
        if(tweets == null) tweets = new HashSet<>();
        tweets.add(tweet);
        tweet.setUser(this);
    }

    public void addRetweet(Retweet retweet){
        if(retweets == null) retweets = new HashSet<>();
        retweets.add(retweet);
        retweet.setUser(this);
    }

    public void addLike(Like like){
        if(likes == null) likes = new HashSet<>();
        likes.add(like);
        like.setUser(this);
    }

    public void addComment(Comment comment){
        if(comments == null) comments = new HashSet<>();
        comments.add(comment);
        comment.setUser(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(obj == null || obj.getClass() != getClass())
            return false;

        User user = (User) obj;

        return user.getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}