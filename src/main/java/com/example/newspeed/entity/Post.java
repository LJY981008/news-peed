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
    private Long postId;

    private String title;

    private String content;
    private Long userLikeCount;
    private boolean isUserLiked;

//    @ManyToOne
//    private Users users;

    //    public void setUsers(Users users) {
//        this.users = users;
//    }

    public void updatePost(String newtitle, String newContents) {
        this.title = newtitle;
        this.content = newContents;
    }
}
