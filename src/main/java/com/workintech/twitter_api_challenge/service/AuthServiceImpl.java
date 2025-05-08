package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.dto.AuthResponse;
import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.RegisterRequest;
import com.workintech.twitter_api_challenge.dto.UserResponse;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TwitterException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import com.workintech.twitter_api_challenge.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findUserByUsername(request.getUsername()).isPresent()) {
            throw new TwitterException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new TwitterException("Email is already registered", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setCreationDate(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        String token = jwtUtil.generateToken(savedUser.getUsername());

        return new AuthResponse(token, new UserResponse(savedUser));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new TwitterException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, new UserResponse(user));
    }
}
