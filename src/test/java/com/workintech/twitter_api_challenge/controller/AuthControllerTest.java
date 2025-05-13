package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.AuthResponse;
import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.RegisterRequest;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TwitterException;
import com.workintech.twitter_api_challenge.exceptions.UserNotFoundException;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import com.workintech.twitter_api_challenge.security.JwtUtil;
import com.workintech.twitter_api_challenge.service.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password1!");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("Password1!");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encoded");
    }

    @Test
    @DisplayName("Kayıt işlemi başarılı olduğunda kullanıcı kaydediliyor mu?")
    void testRegisterSuccess() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("encoded");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(jwtUtil.generateToken(anyString())).willReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Mevcut kullanıcı adı ile kayıt yapılmaya çalışıldığında hata fırlatılıyor mu?")
    void testRegisterUsernameExists() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        assertThrows(TwitterException.class, () -> authService.register(registerRequest));
    }

    @Test
    @DisplayName("Geçerli kullanıcı bilgileriyle giriş yapıldığında token döner mi?")
    void testLoginSuccess() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.generateToken(anyString())).willReturn("jwt-token-login");

        AuthResponse resp = authService.login(loginRequest);
        assertEquals("jwt-token-login", resp.getToken());
    }

    @Test
    @DisplayName("Mevcut olmayan kullanıcıyla giriş yapılmaya çalışıldığında hata fırlatılıyor mu?")
    void testLoginUserNotFound() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    @DisplayName("Yanlış şifre girildiğinde hata fırlatılıyor mu?")
    void testLoginInvalidPassword() {
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
        assertThrows(TwitterException.class, () -> authService.login(loginRequest));
    }
}



