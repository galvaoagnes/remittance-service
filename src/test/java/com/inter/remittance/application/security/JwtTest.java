package com.inter.remittance.application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = new Jwt(SECRET, 60_000L);
    }

    @Test
    void shouldGenerateTokenWithExpectedSubject() {
        UserDetails user = User.withUsername("12345678901").password("pwd").roles("PF").build();

        String token = jwt.generateToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("12345678901", claims.getSubject());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void shouldExtractUsernameAndValidateToken() {
        UserDetails user = User.withUsername("12345678901").password("pwd").roles("PF").build();
        String token = jwt.generateToken(user);

        assertEquals("12345678901", jwt.extractUsername(token));
        assertTrue(jwt.isValid(token, user));
    }
}

