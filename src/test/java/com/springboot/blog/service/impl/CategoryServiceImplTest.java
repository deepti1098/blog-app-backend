package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDTO;
import com.springboot.blog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Tech");
        category.setDescription("Technology-related content");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Tech");
        categoryDTO.setDescription("Technology-related content");
    }

    @Test
    void testAddCategory_ShouldSaveAndReturnCategoryDTO_WhenValidCategoryDTO() {
        when(mapper.map(any(CategoryDTO.class), eq(Category.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO savedCategoryDTO = categoryService.addCategory(categoryDTO);

        assertNotNull(savedCategoryDTO);
        assertEquals("Tech", savedCategoryDTO.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategoryById_ShouldReturnCategoryDTO_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO foundCategoryDTO = categoryService.getCategory(1L);

        assertNotNull(foundCategoryDTO);
        assertEquals("Tech", foundCategoryDTO.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCategories_ShouldReturnListOfCategoryDTOs() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(mapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();

        assertEquals(1, categoryDTOList.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory_ShouldReturnUpdatedCategoryDTO_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.map(any(Category.class), eq(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, 1L);

        assertNotNull(updatedCategoryDTO);
        assertEquals("Tech", updatedCategoryDTO.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory_ShouldRemoveCategory_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    void testGetCategoryById_ShouldThrowResourceNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategory(1L));
    }

    @Test
    void testDeleteCategory_ShouldThrowResourceNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
