package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @Size(min=3,max = 100)
    @NotEmpty(message = "Username is required")
    private String username;

    @NotNull
    @Size(min=5,max = 150)
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull
    @Size(min=8,max = 100)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    @NotEmpty(message = "Password is required")
    private String password;

    private String bio;
    private String profilePhotoUrl;
}
