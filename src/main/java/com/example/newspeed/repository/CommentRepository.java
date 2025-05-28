package com.example.newspeed.repository;

import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Post> findPostById(Long postId);
}
