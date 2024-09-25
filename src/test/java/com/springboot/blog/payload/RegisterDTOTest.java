package com.springboot.blog.payload;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenNameIsEmpty_shouldReturnValidationError() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("");
        registerDTO.setUsername("validUsername");
        registerDTO.setEmail("valid@example.com");
        registerDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void whenUsernameIsTooShort_shouldReturnValidationError() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setUsername("ab"); // Too short
        registerDTO.setEmail("valid@example.com");
        registerDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertEquals(1, violations.size());
        assertEquals("Username must be between 3 and 20 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void whenUsernameIsTooLong_shouldReturnValidationError() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setUsername("verylongusername12345"); // Too long
        registerDTO.setEmail("valid@example.com");
        registerDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertEquals(1, violations.size());
        assertEquals("Username must be between 3 and 20 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void whenEmailIsInvalid_shouldReturnValidationError() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setUsername("validUsername");
        registerDTO.setEmail("invalid-email"); // Invalid email format
        registerDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    public void whenPasswordIsTooShort_shouldReturnValidationError() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setUsername("validUsername");
        registerDTO.setEmail("valid@example.com");
        registerDTO.setPassword("123"); // Too short

        Set<jakarta.validation.ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 6 characters long", violations.iterator().next().getMessage());
    }

    @Test
    public void whenAllFieldsAreValid_shouldNotReturnValidationErrors() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setUsername("validUsername");
        registerDTO.setEmail("valid@example.com");
        registerDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertTrue(violations.isEmpty());
    }
}

