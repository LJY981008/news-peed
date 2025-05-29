package com.example.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "posts")
public class Post extends TimeStampEntity{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

//    public void setUsers(Users users) {
//        this.users = users;
//    }

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true)
    private Long userLikeCount;

    @Column(nullable = false)
    private boolean isUserLiked;
    private String imageUrl;

    // Post생성자
    public Post(String title, String content, String imageUrl, Users users) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.users = users;
    }

    public Post() {

    }
}
