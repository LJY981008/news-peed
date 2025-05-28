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
