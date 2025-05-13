package com.workintech.twitter_api_challenge.service;

import com.workintech.twitter_api_challenge.dto.AuthResponse;
import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.RegisterRequest;
import com.workintech.twitter_api_challenge.entity.User;
import com.workintech.twitter_api_challenge.exceptions.TwitterException;
import com.workintech.twitter_api_challenge.repository.UserRepository;
import com.workintech.twitter_api_challenge.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthServiceImpl authService;

    private RegisterRequest registerReq;
    private LoginRequest loginReq;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encoded");

        registerReq = new RegisterRequest();
        registerReq.setUsername("testuser");
        registerReq.setEmail("test@example.com");
        registerReq.setPassword("Password1!");

        loginReq = new LoginRequest();
        loginReq.setUsername("testuser");
        loginReq.setPassword("Password1!");
    }

    @Test
    @DisplayName("Kayıt işlemi başarılı olduğunda token dönüyor mu?")
    void testRegisterSuccess() {
        when(userRepo.findByUsername(registerReq.getUsername())).thenReturn(Optional.empty());
        when(userRepo.findByEmail(registerReq.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerReq.getPassword())).thenReturn("encoded");
        when(userRepo.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setCreationDate(LocalDateTime.now());
            return u;
        });
        when(jwtUtil.generateToken("testuser")).thenReturn("token");

        AuthResponse resp = authService.register(registerReq);

        assertNotNull(resp.getToken());
        assertEquals("testuser", resp.getUser().getUsername());
    }

    @Test
    @DisplayName("Mevcut kullanıcı adı ile kayıt denendiğinde hata fırlatılıyor mu?")
    void testRegisterUsernameTaken() {
        when(userRepo.findByUsername(registerReq.getUsername())).thenReturn(Optional.of(user));
        assertThrows(TwitterException.class,
                () -> authService.register(registerReq));
        verify(userRepo, never()).save(any());
    }

    @Test
    @DisplayName("Geçerli giriş bilgisi ile login başarılı oluyor mu?")
    void testLoginSuccess() {
        when(userRepo.findByUsername(loginReq.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginReq.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getUsername())).thenReturn("token");

        AuthResponse resp = authService.login(loginReq);

        assertNotNull(resp.getToken());
        assertEquals("testuser", resp.getUser().getUsername());
    }

    @Test
    @DisplayName("Yanlış parola ile login denendiğinde hata fırlatılıyor mu?")
    void testLoginInvalidPassword() {
        when(userRepo.findByUsername(loginReq.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginReq.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(TwitterException.class,
                () -> authService.login(loginReq));
    }
}
