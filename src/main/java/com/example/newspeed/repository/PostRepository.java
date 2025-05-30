package com.example.newspeed.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.newspeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser_UserIdIn(List<Long> followedUserIds, Pageable pageable);

    Page<Post> findAllBycreatedAt(LocalDateTime createdAt, Pageable pageable);
}
