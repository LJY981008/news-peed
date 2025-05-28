package com.example.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "posts")
public class Post extends TimeStampEntity{
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    @Column(columnDefinition = "Long text")
    private String content;
    private Long userLikeCount;
    private boolean isUserLiked;
}
