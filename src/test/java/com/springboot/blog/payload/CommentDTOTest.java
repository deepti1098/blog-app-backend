package com.springboot.blog.payload;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CommentDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenNameIsEmpty_shouldReturnValidationError() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setName(""); // Empty name
        commentDTO.setEmail("test@example.com");
        commentDTO.setBody("This is a valid comment body.");

        Set<jakarta.validation.ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);
        assertEquals(1, violations.size());
        assertEquals("Name should not be null or empty", violations.iterator().next().getMessage());
    }

    @Test
    public void whenEmailIsEmpty_shouldReturnValidationError() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setName("John Doe");
        commentDTO.setEmail(""); // Empty email
        commentDTO.setBody("This is a valid comment body.");

        Set<jakarta.validation.ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);
        assertEquals(1, violations.size());
        assertEquals("Email should not be null or empty", violations.iterator().next().getMessage());
    }

    @Test
    public void whenEmailIsInvalid_shouldReturnValidationError() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setName("John Doe");
        commentDTO.setEmail("invalid-email"); // Invalid email format
        commentDTO.setBody("This is a valid comment body.");

        Set<jakarta.validation.ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Please provide a valid email address"));
    }

    @Test
    public void whenBodyIsTooShort_shouldReturnValidationError() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setName("John Doe");
        commentDTO.setEmail("test@example.com");
        commentDTO.setBody("Short"); // Less than 10 characters

        Set<jakarta.validation.ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);
        assertEquals(1, violations.size());
        assertEquals("Comment body must be minimum 10 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void whenAllFieldsAreValid_shouldNotReturnValidationErrors() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setName("John Doe");
        commentDTO.setEmail("test@example.com");
        commentDTO.setBody("This is a valid comment body with more than 10 characters.");

        Set<jakarta.validation.ConstraintViolation<CommentDTO>> violations = validator.validate(commentDTO);
        assertTrue(violations.isEmpty());
    }
}

