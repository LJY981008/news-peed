package com.example.newspeed.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupUserRequestDto {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Not entered email")
    private String email;

    @NotBlank(message = "Not entered password")
    private String password;

    @NotBlank(message = "Not entered userName")
    private String userName;

    private String intro;

    private String profileImageUrl;
}