package com.inter.remittance.application.service;

import com.inter.remittance.security.Jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final Jwt jwt;

    public AuthService(AuthenticationManager authenticationManager, Jwt jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwt = jwtService;
    }

    public String login(String document, String password) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(document, password)
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        assert user != null;
        return jwt.generateUserToken(user);
    }
}