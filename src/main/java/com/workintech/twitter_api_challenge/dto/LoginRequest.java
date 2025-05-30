package com.workintech.twitter_api_challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    @NotNull
    @NotEmpty(message = "Username is required")
    private String username;

    @NotNull
    @NotEmpty(message = "Password is required")
    private String password;
}
