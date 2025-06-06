package com.example.newspeed.dto.post;

import com.example.newspeed.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시글 조회 관련 Dto
 * @author 김태현
 */

@Getter
@AllArgsConstructor
public class FindPostResponseDto {
    private final Long postId;
    private final String userName;
    private final String title;
    private final String contents;
    private final String postImgUrl;
    private final int userLikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public FindPostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.userName = post.getUser().getUserName();
        this.title = post.getTitle();
        this.contents = post.getContent();
        this.postImgUrl = post.getUser().getProfileImageUrl();
        this.userLikeCount = post.getUserLikeCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

    public static FindPostResponseDto findPostDto(Post post) {
        return new FindPostResponseDto(
                post.getPostId(),
                post.getUser().getUserName(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getProfileImageUrl(),
                post.getUserLikeCount(),
                post.getCreatedAt(),
                post.getModifiedAt()

        );
    }
}
