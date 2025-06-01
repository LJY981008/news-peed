package com.example.newspeed.dto.post;

import com.example.newspeed.entity.Post;
import lombok.Getter;

/**
 * <p>좋아요 응답</p>
 *
 * @author 이준영
 */
@Getter
public class GetLikeResponseDto {
    private final Long postId;
    private final int likeCount;

    public GetLikeResponseDto(Post post) {
        this.postId = post.getPostId();
        this.likeCount = post.getUserLikeCount() < 0 ? 0 : post.getUserLikeCount();
    }
}
