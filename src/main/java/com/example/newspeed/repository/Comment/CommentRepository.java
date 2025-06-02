package com.example.newspeed.repository.Comment;

import com.example.newspeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>댓글 관련 레포지토리</p>
 *
 * @author 이준영
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
