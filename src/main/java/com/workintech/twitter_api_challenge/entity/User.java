package com.workintech.twitter_api_challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "twitter_api")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 100)
    @Column(name = "username")
    private String username;

    @NotNull
    @NotEmpty
    @Size(max = 150)
    @Column(name = "email")
    private String email;

    @NotNull
    @NotEmpty
    @Size(max = 250)
    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;
}