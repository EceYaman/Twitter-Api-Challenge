package com.workintech.twitter_api_challenge.controller;

import com.workintech.twitter_api_challenge.dto.AuthResponse;
import com.workintech.twitter_api_challenge.dto.LoginRequest;
import com.workintech.twitter_api_challenge.dto.RegisterRequest;
import com.workintech.twitter_api_challenge.dto.UserResponse;
import com.workintech.twitter_api_challenge.exceptions.TwitterException;
import com.workintech.twitter_api_challenge.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest regReq;
    private LoginRequest logReq;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        regReq = new RegisterRequest();
        regReq.setUsername("user1");
        regReq.setEmail("user1@example.com");
        regReq.setPassword("Pass@123");

        logReq = new LoginRequest();
        logReq.setUsername("user1");
        logReq.setPassword("Pass@123");
    }

    @Test
    @DisplayName("Kayıt işlemi başarılı olduğunda 201 dönüyor mu?")
    void testRegisterReturnsCreatedAndToken() {
        UserResponse ur = new UserResponse(); ur.setId(5L); ur.setUsername("user1");
        AuthResponse expected = new AuthResponse("tokken123", ur);
        when(authService.register(eq(regReq))).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.register(regReq);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getToken()).isEqualTo("tokken123");
    }

    @Test
    @DisplayName("Kayıt sırasında kullanıcı adı zaten varsa hata fırlatılıyor mu?")
    void testRegisterWhenUsernameTakenThrowsException() {
        when(authService.register(any())).thenThrow(new TwitterException("Username is already taken", HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> authController.register(regReq))
                .isInstanceOf(TwitterException.class)
                .hasMessageContaining("Username is already taken");
    }

    @Test
    @DisplayName("Giriş işlemi başarılı olduğunda 200 ve kullanıcı bilgisi dönüyor mu?")
    void testLoginReturnsOkAndUserInfo() {
        UserResponse ur = new UserResponse(); ur.setId(5L); ur.setUsername("user1");
        AuthResponse expected = new AuthResponse("tokkenABC", ur);
        when(authService.login(eq(logReq))).thenReturn(expected);

        ResponseEntity<AuthResponse> response = authController.login(logReq);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getUser().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Giriş sırasında geçersiz parola girilirse hata fırlatılıyor mu?")
    void testLoginWhenInvalidCredentialsThrowsException() {
        when(authService.login(any())).thenThrow(new TwitterException("Invalid username or password", HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> authController.login(logReq))
                .isInstanceOf(TwitterException.class)
                .hasMessageContaining("Invalid username or password");
    }
}



