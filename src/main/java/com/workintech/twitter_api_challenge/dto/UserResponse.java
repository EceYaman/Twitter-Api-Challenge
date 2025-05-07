package com.workintech.twitter_api_challenge.dto;

import com.workintech.twitter_api_challenge.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
}
