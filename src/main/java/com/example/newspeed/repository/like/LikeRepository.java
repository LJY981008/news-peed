package com.example.newspeed.repository.like;

import com.example.newspeed.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 좋아요 엔티티의 기본적인 CRUD 작업을 위한 레포지토리
 *
 * @author 이준영
 */
public interface LikeRepository extends JpaRepository<PostLike, Long>, LikeRepositoryCustom {
}
