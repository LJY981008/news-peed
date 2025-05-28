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

    public void updateContent(String content) {
        this.content = content;
    }

    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    //    @ManyToOne
//    @JoinColumn(name = "writer_id")
//    private User writer;
}
