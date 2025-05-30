package com.example.newspeed.repository;

import com.example.newspeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByUserName(String usrName);
    boolean existsByEmail(String email);
}
