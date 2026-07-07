package com.inter.remittance.application.service;

import com.inter.remittance.application.security.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Jwt jwt;

    @Mock
    private Authentication authentication;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(authenticationManager, jwt);
    }

    @Test
    void shouldAuthenticateAndGenerateToken() {
        UserDetails user = User.withUsername("12345678901")
                .password("secret")
                .roles("PF")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwt.generateToken(user)).thenReturn("token-123");

        String token = service.login("12345678901", "Valid@123");

        assertEquals("token-123", token);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertEquals("12345678901", captor.getValue().getPrincipal());
        assertEquals("Valid@123", captor.getValue().getCredentials());
        verify(jwt).generateToken(user);
    }
}

