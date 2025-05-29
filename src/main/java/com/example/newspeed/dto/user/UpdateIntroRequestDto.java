package com.example.newspeed.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateIntroRequestDto {
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @Size(max = 100, message = "소개란은 최대 100자입니다.")
    private String intro;
}
