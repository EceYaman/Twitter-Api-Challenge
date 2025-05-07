package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    @Size(min = 3, max = 100)
    private String username;

    @Email
    private String email;

    private String bio;

    private String profilePhotoUrl;
}
