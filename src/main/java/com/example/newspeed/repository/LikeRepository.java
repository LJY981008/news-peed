package com.example.newspeed.repository;

import com.example.newspeed.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, Long>, LikeRepositoryCustom {
}
