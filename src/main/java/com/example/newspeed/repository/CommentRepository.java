package com.example.newspeed.repository;

import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * <p>댓글 관련 레포지토리</p>
 *
 * @author 이준영
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
