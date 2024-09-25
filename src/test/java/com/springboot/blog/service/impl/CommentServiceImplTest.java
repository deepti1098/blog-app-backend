package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    CommentServiceImpl commentService;
    private Comment comment;
    private CommentDTO commentDTO;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        post = new Post();
        post.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setName("John");
        comment.setEmail("john@example.com");
        comment.setBody("Nice post");
        comment.setPost(post);

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setName("John");
        commentDTO.setEmail("john@example.com");
        commentDTO.setBody("Nice post");
    }

    @Test
    void testCreateComment_ShouldReturnCommentDTO_WhenValidInput(){
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(mapper.map(any(CommentDTO.class), eq(Comment.class))).thenReturn(comment);
        when(mapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        CommentDTO result=commentService.createComment(1L, commentDTO);

        assertEquals(commentDTO.getName(), result.getName());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testGetCommentsByPostId_ShouldReturnListOfComments() {
        when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment));
        when(mapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        List<CommentDTO> result = commentService.getCommentsByPostId(1L);

        assertEquals(1, result.size());
        assertEquals(commentDTO.getName(), result.get(0).getName());
    }

    @Test
    void testGetCommentById_ShouldReturnComment_WhenCommentBelongsToPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(mapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.getCommentById(1L, 1L);

        assertEquals(commentDTO.getName(), result.getName());
    }

    @Test
    void testUpdateComment_ShouldReturnUpdatedComment_WhenValidInput() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(mapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.updateComment(1L, 1L, commentDTO);

        assertEquals(commentDTO.getName(), result.getName());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_ShouldDeleteComment_WhenValidInput() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, 1L);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testGetCommentById_ShouldThrowBlogAPIException_WhenCommentDoesNotBelongToPost() {
        Post anotherPost = new Post();
        anotherPost.setId(2L);
        comment.setPost(anotherPost);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        BlogAPIException exception = assertThrows(BlogAPIException.class, () -> commentService.getCommentById(1L, 1L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

    }

}



