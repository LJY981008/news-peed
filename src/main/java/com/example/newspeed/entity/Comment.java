package com.example.newspeed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends TimeStampEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Comment() {
    }

    public Comment(String content, Post post, Users user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
