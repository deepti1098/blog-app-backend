package com.springboot.blog.payload;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PostDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenTitleIsEmpty_shouldReturnValidationError() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(null);
        postDTO.setDescription("A long description");
        postDTO.setContent("Some content");


        Set<jakarta.validation.ConstraintViolation<PostDTO>> violations = validator.validate(postDTO);
        assertEquals(1, violations.size());
        assertEquals("must not be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void whenDescriptionIsTooShort_shouldReturnValidationError() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Title");
        postDTO.setDescription("Short");
        postDTO.setContent("Some content");

        Set<jakarta.validation.ConstraintViolation<PostDTO>> violations = validator.validate(postDTO);
        assertEquals(1, violations.size());
        assertEquals("Post title should be at least 10 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void whenContentIsEmpty_shouldReturnValidationError() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Valid Title");
        postDTO.setDescription("A valid description");
        postDTO.setContent(null);

        Set<jakarta.validation.ConstraintViolation<PostDTO>> violations = validator.validate(postDTO);
        assertEquals(1, violations.size());
        assertEquals("must not be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void whenAllFieldsAreValid_shouldNotReturnValidationErrors() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Valid Title");
        postDTO.setDescription("A valid description with at least 10 characters.");
        postDTO.setContent("Some content");

        Set<jakarta.validation.ConstraintViolation<PostDTO>> violations = validator.validate(postDTO);
        assertTrue(violations.isEmpty());
    }
}
