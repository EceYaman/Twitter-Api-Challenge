package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.dto.AuthResponse;
import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
