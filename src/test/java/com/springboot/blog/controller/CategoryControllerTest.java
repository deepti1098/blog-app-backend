package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.CategoryDTO;
import com.springboot.blog.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Tech");
        categoryDTO.setDescription("Technology-related content");
    }

    @Test
    void testAddCategory_ShouldReturnCreatedCategory_WhenValidInput() throws Exception {
        when(categoryService.addCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    void testGetCategory_ShouldReturnCategory_WhenCategoryExists() throws Exception {
        when(categoryService.getCategory(1L)).thenReturn(categoryDTO);

        mockMvc.perform(get("/api/categories/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    void testGetAllCategories_ShouldReturnListOfCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryDTO));

        mockMvc.perform(get("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Tech"));
    }

    @Test
    void testUpdateCategory_ShouldReturnUpdatedCategory_WhenCategoryExists() throws Exception{
        when(categoryService.updateCategory(any(CategoryDTO.class), eq(1L))).thenReturn(categoryDTO);

        mockMvc.perform(put("/api/categories/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    void testDeleteCategory_ShouldReturnSuccessMessage_WhenCategoryExists() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("category deleted successfully!!!"));
    }

}
