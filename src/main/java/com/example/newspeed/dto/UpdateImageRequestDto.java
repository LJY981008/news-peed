package com.example.newspeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateImageRequestDto {
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "올바른 이메일 형식이 아님")
    private String email;
    @Pattern(regexp = "^(https?:\\/\\/)?([\\w\\-])+\\.[\\w\\-]+(\\/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif)$",
            message = "올바른 이미지 URL 형식이 아닙니다.") // (http(s)://)example.com/image.jpg 형식
    private String profileImage;
}
