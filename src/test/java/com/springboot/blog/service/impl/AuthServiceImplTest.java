package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.LoginDTO;
import com.springboot.blog.payload.RegisterDTO;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;
    private LoginDTO loginDTO;
    private RegisterDTO registerDTO;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginDTO = new LoginDTO();
        loginDTO.setUsernameOrEmail("testuser@example.com");
        loginDTO.setPassword("password");

        registerDTO = new RegisterDTO();
        registerDTO.setName("Test User");
        registerDTO.setUsername("testuser");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setPassword("password");

        user = new User();
        user.setName("Test User");
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");

        role = new Role();
        role.setName("ROLE_USER");
    }

    @Test
    void testLogin_ShouldReturnJwtToken_WhenValidCredentials(){
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("mock-jwt-token");

        String token = authService.login(loginDTO);

        assertEquals("mock-jwt-token", token);
        verify(authenticationManager,times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(any(Authentication.class));

    }

    @Test
    void testLogin_ShouldThrowException_WhenInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, never()).generateToken(any(Authentication.class));
    }

    @Test
    void testRegister_ShouldRegisterUser_WhenValidInput(){
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("testuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded-password");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = authService.register(registerDTO);

        assertEquals("register user successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(roleRepository, times(1)).findByName("ROLE_USER");
    }

    @Test
    void testRegister_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        BlogAPIException exception = assertThrows(BlogAPIException.class, () -> {
            authService.register(registerDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username already exits", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmail("testuser@example.com")).thenReturn(true);

        BlogAPIException exception = assertThrows(BlogAPIException.class, () -> {
            authService.register(registerDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email Id already exits", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
