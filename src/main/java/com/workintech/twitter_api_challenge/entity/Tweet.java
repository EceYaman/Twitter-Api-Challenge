package com.workintech.twitter_api_challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "tweet", schema = "twitter_api")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "content")
    private String content;

    @Column(name = "image_url")
    private LocalDateTime imageUrl;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @NotNull
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "tweet")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private Set<Retweet> retweets = new HashSet<>();

    public void addComment(Comment comment){
        if(comments == null) comments = new HashSet<>();
        comments.add(comment);
    }

    public void addLike(Like like){
        if(likes == null) likes = new HashSet<>();
        likes.add(like);
    }

    public void addRetweet(Retweet retweet){
        if(retweets == null) retweets = new HashSet<>();
        retweets.add(retweet);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(obj == null || obj.getClass() != getClass())
            return false;

        Tweet tweet = (Tweet) obj;

        return tweet.getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}
