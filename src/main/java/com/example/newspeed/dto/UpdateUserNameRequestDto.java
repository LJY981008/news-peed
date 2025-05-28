package com.example.newspeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserNameRequestDto {
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "이름은 필수 입력사항입니다.")
    @Size(max = 20, message = "이름은 최대 20자입니다.")
    private String userName;
    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    @Size(min = 4, max = 20, message = "비밀번호의 길이는 4~20자입니다.")
    private String password;
}
