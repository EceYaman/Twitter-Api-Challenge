package com.workintech.twitter_api_challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "like", schema = "twitter_api")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "user_id")
    private User user;

    @NotNull
    @NotEmpty
    @Column(name = "tweet_id")
    private Tweet tweet;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
