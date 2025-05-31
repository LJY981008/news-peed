package com.example.newspeed.repository.comment;

import com.example.newspeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>댓글 관련 레포지토리</p>
 *
 * @author 이준영
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
