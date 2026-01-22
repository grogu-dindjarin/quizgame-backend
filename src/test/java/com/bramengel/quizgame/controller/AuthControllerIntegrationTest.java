package com.bramengel.quizgame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_withValidRequest_returns200WithToken() throws Exception {
        Map<String, String> body = Map.of(
                "email", "newuser@example.com",
                "displayName", "New User",
                "password", "securepass123"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.displayName").value("New User"));
    }

    @Test
    void register_withDuplicateEmail_returns400() throws Exception {
        Map<String, String> first = Map.of(
                "email", "duplicate@example.com",
                "displayName", "First",
                "password", "securepass123"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(first)))
                .andExpect(status().isOk());

        Map<String, String> second = Map.of(
                "email", "duplicate@example.com",
                "displayName", "Second",
                "password", "securepass123"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("E-mailadres is al in gebruik"));
    }

    @Test
    void login_withValidCredentials_returns200WithToken() throws Exception {
        Map<String, String> registerBody = Map.of(
                "email", "logintest@example.com",
                "displayName", "Login Test",
                "password", "securepass123"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody)))
                .andExpect(status().isOk());

        Map<String, String> loginBody = Map.of(
                "email", "logintest@example.com",
                "password", "securepass123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("logintest@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_withWrongPassword_returns400() throws Exception {
        Map<String, String> registerBody = Map.of(
                "email", "wrongpass@example.com",
                "displayName", "Wrong Pass",
                "password", "securepass123"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody)))
                .andExpect(status().isOk());

        Map<String, String> loginBody = Map.of(
                "email", "wrongpass@example.com",
                "password", "totallyWrongPassword"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ongeldig e-mailadres of wachtwoord"));
    }
}
