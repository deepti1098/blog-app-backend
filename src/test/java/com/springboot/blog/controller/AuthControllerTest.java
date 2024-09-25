package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.LoginDTO;
import com.springboot.blog.payload.RegisterDTO;
import com.springboot.blog.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private LoginDTO loginDTO;
    private RegisterDTO registerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        loginDTO = new LoginDTO();
        loginDTO.setUsernameOrEmail("testuser@example.com");
        loginDTO.setPassword("password");

        registerDTO = new RegisterDTO();
        registerDTO.setName("Test User");
        registerDTO.setUsername("testuser");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setPassword("password");
    }

    @Test
    void testLogin_ShouldReturnToken_WhenValidCredentials() throws Exception {
        String mockToken = "mock-jwt-token";
        when(authService.login(any(LoginDTO.class))).thenReturn(mockToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(mockToken));
    }

    @Test
    void testLogin_ShouldReturnUnauthorized_WhenInvalidCredentials() throws Exception {
        when(authService.login(any(LoginDTO.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_ShouldReturnBadRequest_WhenMissingFields() throws Exception {
        LoginDTO invalidLoginDTO = new LoginDTO();
        // No email or password provided

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidLoginDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testRegister_ShouldReturnCreatedMessage_WhenValidInput() throws Exception {
        when(authService.register(any(RegisterDTO.class))).thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("User registered successfully"));
    }

    @Test
    void testRegister_ShouldReturnBadRequest_WhenUsernameExists() throws Exception {
        when(authService.register(any(RegisterDTO.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void testRegister_ShouldReturnBadRequest_WhenEmailExists() throws Exception {
        when(authService.register(any(RegisterDTO.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    void testRegister_ShouldReturnBadRequest_WhenMissingFields() throws Exception {
        RegisterDTO invalidRegisterDTO = new RegisterDTO();
        // No username, email or password provided

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRegisterDTO)))
                .andExpect(status().isBadRequest());
    }

}
