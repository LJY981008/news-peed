package com.example.newspeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserNameRequestDto {
    @NotBlank
    @Size
    private String userName;
    @NotBlank
    @Size
    private String password;
}
