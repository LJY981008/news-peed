package com.example.newspeed.repository;

import com.example.newspeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>게시글 Repository</p>
 *
 * @author 윤희준
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Object> findByPostId(Long postId);

    Page<Post> findByUser_UserIdIn(List<Long> followedUserIds, Pageable pageable);
}
