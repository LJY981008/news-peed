package com.example.newspeed.dto.post;

import com.example.newspeed.entity.Post;
import lombok.Getter;

@Getter
public class GetLikeResponseDto {
    private Long postId;
    private int likeCount;

    public GetLikeResponseDto(Post post) {
        this.postId = post.getPostId();
        this.likeCount = post.getUserLikeCount();
    }
}
