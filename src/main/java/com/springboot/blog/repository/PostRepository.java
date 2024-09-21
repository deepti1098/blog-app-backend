package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository is not required actually, because the SimpleJpaRepository class already annotated with @Repository
public interface PostRepository extends JpaRepository<Post, Long> {

 List<Post> findByCategoryId(Long categoryId);

}
