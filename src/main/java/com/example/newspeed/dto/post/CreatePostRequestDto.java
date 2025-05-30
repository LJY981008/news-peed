package com.example.newspeed.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>게시글 생성 요청 DTO</p>
 *
 * @author 윤희준
 */
@RequiredArgsConstructor
@Getter
public class CreatePostRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private final String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private final String content;
    @Pattern(regexp = "^.*\\.(?i)(jpg|jpeg|png|gif)", message = "유효한 이미지 형식이 아닙니다.")
    private final String imageUrl;

}
