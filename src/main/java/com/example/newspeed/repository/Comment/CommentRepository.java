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
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_PostIdAndDeletedFalse(Long postId);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE c.post.postId = :postId AND c.deleted = false")
    List<Comment> findByPostIdWithUser(@Param("postId") Long postId);
}
