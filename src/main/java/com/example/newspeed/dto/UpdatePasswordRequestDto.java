package com.example.newspeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordRequestDto {
    @NotBlank
    @Size
    private String originPassword;
    @NotBlank
    @Size
    private String newPassword;
}
