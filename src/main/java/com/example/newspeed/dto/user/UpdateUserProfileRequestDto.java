package com.example.newspeed.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserProfileRequestDto {
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @Size(min = 4, max = 20, message = "비밀번호의 길이는 4~20자입니다.")
    private String password;
    @Size(max = 20, message = "이름은 최대 20자입니다.")
    private String userName;
    @Size(min = 4, max = 20, message = "비밀번호의 길이는 4~20자입니다.")
    private String newPassword;
    @Size(max = 100, message = "소개란은 최대 100자입니다.")
    private String intro;
    @Pattern(regexp = "^(https?:\\/\\/)?([\\w\\-])+\\.[\\w\\-]+(\\/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif)$",
            message = "올바른 이미지 URL 형식이 아닙니다.") // (http(s)://)example.com/image.jpg 형식
    private String profileImage;
}
