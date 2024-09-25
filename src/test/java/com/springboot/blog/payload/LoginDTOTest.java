package com.springboot.blog.payload;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenUsernameOrEmailIsEmpty_shouldReturnValidationError() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsernameOrEmail(""); // Empty
        loginDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertEquals(1, violations.size());
        assertEquals("Username or Email is required", violations.iterator().next().getMessage());
    }

    @Test
    public void whenPasswordIsEmpty_shouldReturnValidationError() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsernameOrEmail("validUser@example.com");
        loginDTO.setPassword(""); // Empty

        Set<jakarta.validation.ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    public void whenAllFieldsAreValid_shouldNotReturnValidationErrors() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsernameOrEmail("validUser@example.com");
        loginDTO.setPassword("validPassword");

        Set<jakarta.validation.ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.isEmpty());
    }
}

