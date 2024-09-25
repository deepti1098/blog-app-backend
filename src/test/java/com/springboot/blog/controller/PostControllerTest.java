package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PostControllerTest {
    @InjectMocks
    private PostController postController;
    @Mock
    private PostService postService;

    private PostDTO postDTO;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Test Title");
        postDTO.setDescription("Test Description");
        postDTO.setContent("Test Content");
        postDTO.setCategoryId(1L);
    }

    @Test
    public void createPost_ShouldReturnCreatedPost(){
        when(postService.createPost(any(PostDTO.class))).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.createPost(postDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postDTO,response.getBody());
        verify(postService).createPost(postDTO);
    }

    @Test
    void getAllPosts_ShouldReturnPostResponse(){
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(Collections.singletonList(postDTO));
        when(postService.getAllPosts(0,10,"title", "asc")).thenReturn(postResponse);

        PostResponse response = postController.getAllPosts(0,10,"title","asc");

        assertEquals(postResponse, response);
        verify(postService).getAllPosts(0,10,"title", "asc");
    }

    @Test
    void getPostById_ShouldReturnPost() {
        when(postService.getPostById(1L)).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.getPostById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
        verify(postService).getPostById(1L);
    }

    @Test
    void updatePost_ShouldReturnUpdatedPost() {
        when(postService.updatePost(any(PostDTO.class), eq(1L))).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.updatePOST(postDTO, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
        verify(postService).updatePost(any(PostDTO.class), eq(1L));
    }

    @Test
    void deletePost_ShouldReturnSuccessMessage() {
        doNothing().when(postService).deletePostById(1L);
        ResponseEntity<String> response = postController.deletePOST(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post entity deleted successfully!!", response.getBody());
        verify(postService).deletePostById(1L);
    }

    @Test
    void getPostsByCategory_ShouldReturnListOfPosts() {
        when(postService.getPostsByCategory(1L)).thenReturn(Collections.singletonList(postDTO));
        ResponseEntity<List<PostDTO>> response = postController.getPostsByCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(postDTO), response.getBody());
        verify(postService).getPostsByCategory(1L);
    }

}
