package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@Repository is not required actually, because the SimpleJpaRepository class already annotated with @Repository
public interface PostRepository extends JpaRepository<Post, Long> {

 List<Post> findByCategoryId(Long categoryId);

 @Query("SELECT p FROM Post p WHERE " +
         "p.title LIKE CONCAT('%', :keyword, '%') OR " +
         "p.description LIKE CONCAT('%', :keyword, '%') OR " +
         "p.content LIKE CONCAT('%', :keyword, '%')")
 List<Post> searchPost(String keyword);

}
