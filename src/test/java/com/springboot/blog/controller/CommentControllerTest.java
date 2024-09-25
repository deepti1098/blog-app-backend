package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentControllerTest {
    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setName("John Doe");
        commentDTO.setBody("This is a valid comment body.");
        commentDTO.setEmail("john@example.com");
    }

    @Test
    void testCreateComment_ShouldReturnCreatedComment_WhenValidInput() throws Exception {
        when(commentService.createComment(any(Long.class), any(CommentDTO.class))).thenReturn(commentDTO);

        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.body").value("This is a valid comment body."))
                .andExpect(jsonPath("$.email").value("john@example.com"));;
    }
    @Test
    void testGetCommentsByPostId_ShouldReturnListOfComments() throws Exception {
        when(commentService.getCommentsByPostId(1L)).thenReturn(Arrays.asList(commentDTO));

        mockMvc.perform(get("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetCommentById_ShouldReturnComment_WhenCommentExists() throws Exception {
        when(commentService.getCommentById(any(Long.class), any(Long.class))).thenReturn(commentDTO);

        mockMvc.perform(get("/api/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
    @Test
    void testUpdateComment_ShouldReturnUpdatedComment_WhenValidInput() throws Exception {
        when(commentService.updateComment(any(Long.class), any(Long.class), any(CommentDTO.class))).thenReturn(commentDTO);

        mockMvc.perform(put("/api/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
    @Test
    void testDeleteComment_ShouldReturnSuccessMessage_WhenValidInput() throws Exception {
        mockMvc.perform(delete("/api/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully!!"));
    }
}
