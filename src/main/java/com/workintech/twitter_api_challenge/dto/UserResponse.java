package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profilePhotoUrl;
    private LocalDateTime creationDate;
    public UserResponse(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.bio = u.getBio();
        this.profilePhotoUrl = u.getProfilePhotoUrl();
        this.creationDate = u.getCreationDate();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public LocalDateTime getCreationDate() { return creationDate; }
}
