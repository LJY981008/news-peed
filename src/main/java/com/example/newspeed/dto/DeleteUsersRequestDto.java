package com.example.newspeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DeleteUsersRequestDto {
    @NotBlank
    @Size
    private String password;
}
