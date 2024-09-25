package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostServiceImplTest {

    // Mock dependencies
    @Mock
    private PostRepository postRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper mapper;

    // Inject mocked dependencies into the service implementation
    @InjectMocks
    private PostServiceImpl postService;

    // Variables to be used in tests
    private Category category;
    private Post post;
    private PostDTO postDTO;

    @BeforeEach
    void setUp(){
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);

        // Sample data for tests
        category = new Category(1L, "Tech", "Technology related posts", null);
        post = new Post(1L, "Post Title", "Post Description", "Post Content", null, category);
        postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Post Title");
        postDTO.setDescription("Post Description");
        postDTO.setContent("Post Content");
        postDTO.setCategoryId(1L);
    }

    // Test for creating a new post
    @Test
    void shouldCreatePost_whenCategoryExists() {
        // Mocking behavior for the repository and mapper
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.map(postDTO, Post.class)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        // Calling the service method
        PostDTO createdPost = postService.createPost(postDTO);

        // Validating the response
        assertNotNull(createdPost);
        assertEquals("Post Title", createdPost.getTitle());

        // Verifying interactions with mocks
        verify(categoryRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    // Test for getting a post by ID
    @Test
    void shouldReturnPostById_whenPostExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        PostDTO foundPost = postService.getPostById(1L);

        assertNotNull(foundPost);
        assertEquals("Post Title", foundPost.getTitle());
        verify(postRepository).findById(1L);
    }

    // Test for retrieving all posts with pagination
    @Test
    void shouldReturnAllPosts_whenPostsExist() {
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post));
        when(postRepository.findAll(any(Pageable.class))).thenReturn(postPage);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        PostResponse postResponse = postService.getAllPosts(0, 10, "id", "asc");

        assertNotNull(postResponse);
        assertEquals(1, postResponse.getContent().size());
        assertEquals(0, postResponse.getPageNo());
        assertTrue(postResponse.getContent().stream().anyMatch(p -> p.getTitle().equals("Post Title")));
        verify(postRepository).findAll(any(Pageable.class));
    }

    // Test for updating an existing post
    @Test
    void shouldUpdatePost_whenPostExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.map(postDTO, Post.class)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        PostDTO updatedPost = postService.updatePost(postDTO, 1L);

        assertNotNull(updatedPost);
        assertEquals("Post Title", updatedPost.getTitle());
        verify(postRepository).save(post);
    }

    // Test for deleting a post by ID
    @Test
    void shouldDeletePostById_whenPostExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePostById(1L);

        verify(postRepository).delete(post);
    }

    // Test for getting posts by category
    @Test
    void shouldReturnPostsByCategory_whenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(postRepository.findByCategoryId(1L)).thenReturn(Collections.singletonList(post));
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        List<PostDTO> posts = postService.getPostsByCategory(1L);

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Post Title", posts.get(0).getTitle());
        verify(postRepository).findByCategoryId(1L);
    }

    // Test for creating a post when the category is not found
    @Test
    void shouldThrowException_whenCategoryNotFoundWhileCreatingPost() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> postService.createPost(postDTO));
        assertEquals("Category not found with id: 1", exception.getMessage());
    }

    // Test for updating a post when the post is not found
    @Test
    void shouldThrowException_whenPostNotFoundWhileUpdating() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(postDTO, 1L));
        assertEquals("post not found with id: 1", exception.getMessage());
    }

    // Test for getting a post by ID when the post is not found
    @Test
    void shouldThrowException_whenPostNotFoundWhileRetrieving() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
        assertEquals("post not found with id: 1", exception.getMessage());
    }
}
