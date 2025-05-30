package com.example.newspeed.repository;

import com.example.newspeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser_UserIdIn(List<Long> followedUserIds, Pageable pageable);

    /**
     * 게시글 날짜로 조회할때 사용하는 레파지토리
     * @author 김태현
     * @param startTime
     * @param endTime
     * @param pageable
     * @return
     */
    Page<Post> findAllByCreatedAtBetween(LocalDateTime startTime,LocalDateTime endTime, Pageable pageable);
}
