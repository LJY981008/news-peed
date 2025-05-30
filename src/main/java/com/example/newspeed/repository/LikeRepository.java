package com.example.newspeed.repository;

import com.example.newspeed.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>좋아요 테이블 레포지토리</p>
 *
 * @author 이준영
 */
public interface LikeRepository extends JpaRepository<PostLike, Long>, LikeRepositoryCustom {
}
