package com.example.newspeed.entity;

import com.example.newspeed.dto.post.UpdatePostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "posts")
public class Post extends TimeStampEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes;

//    public void setUsers(User user) {
//        this.user = user;
//    }

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "integer default 0")
    private int userLikeCount = 0;

    @Column(nullable = false)
    private boolean isUserLiked;
    private String imageUrl;

    // Post생성자
    public Post(String title, String content, String imageUrl, User user) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public Post() {

    }

    public void updatePost(UpdatePostRequestDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContents();
    }

    public void updateLike(boolean trigger){
        this.userLikeCount = trigger ?  this.userLikeCount + 1 : this.userLikeCount - 1;
        if(this.userLikeCount < 0){
            this.userLikeCount = 0;
        }
    }
}
