package com.example.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "posts")
public class Post extends TimeStampEntity{

    @ManyToOne
    private Users users;

    public void setUsers(Users users) {
        this.users = users;
    }

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String title;

    @Column(columnDefinition = "Long text")
    private String content;
    private Long userLikeCount;
    private boolean isUserLiked;
}
