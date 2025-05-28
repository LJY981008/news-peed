package com.example.newspeed.dto.Post;

import com.example.newspeed.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindPostResponseDto {
    private final Long userId;
    private final String userName;
    private final String title;
    private final String contents;
    private final String postImgUrl;
    private final Long userLikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public FindPostResponseDto(Post post) {
        this.userId = post.getUserId();
        this.userName = post.getUsers().getUserName();
        this.title = post.getTitle();
        this.contents = post.getContent();
        this.postImgUrl = post.getUsers().getProfileImageUrl();
        this.userLikeCount = post.getUserLikeCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

    public static FindPostResponseDto findPostDto(Post post) {
        return new FindPostResponseDto(
                post.getUserId(),
                post.getUsers().getUserName(),
                post.getTitle(),
                post.getContent(),
                post.getUsers().getProfileImageUrl(),
                post.getUserLikeCount(),
                post.getCreatedAt(),
                post.getModifiedAt()

        );
    }
}
