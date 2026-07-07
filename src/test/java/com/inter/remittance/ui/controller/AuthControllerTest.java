package com.inter.remittance.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inter.remittance.application.security.JwtFilter;
import com.inter.remittance.application.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void shouldReturn200WithTokenWhenCredentialsAreValid() throws Exception {
        when(authService.login("12345678901", "Valid@123")).thenReturn("jwt-token-abc");

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "document", "12345678901",
                                "password", "Valid@123"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-abc"));
    }

    @Test
    void shouldReturn400WhenDocumentIsMissing() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "password", "Valid@123"
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn400WhenPasswordIsMissing() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "document", "12345678901"
                        ))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenBodyIsMalformedJson() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("{bad json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn500WhenServiceThrowsUnexpectedException() throws Exception {
        when(authService.login("12345678901", "Valid@123"))
                .thenThrow(new BadCredentialsException("bad creds"));

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "document", "12345678901",
                                "password", "Valid@123"
                        ))))
                .andExpect(status().is5xxServerError());
    }
}

