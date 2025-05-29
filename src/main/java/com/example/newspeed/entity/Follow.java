package com.example.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;
    @JoinColumn(name = "following_user_id", nullable = false)
    private Long followingUserId;
    @JoinColumn(name = "followed_user_id", nullable = false)
    private Long followedUserId;

    public Follow(Long followingUserId, Long followedUserId){
        this.followingUserId = followingUserId;
        this.followedUserId = followedUserId;
    }
}
