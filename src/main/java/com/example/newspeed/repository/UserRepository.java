package com.example.newspeed.repository;

import com.example.newspeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * <p>유저 테이블 레포지토리</p>
 *
 * @author 이현하
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedFalse(String email);
    List<User> findByUserNameContainingAndDeletedFalse(String name);
    List<User> findByDeletedFalse();
    boolean existsByEmail(String email);
}
