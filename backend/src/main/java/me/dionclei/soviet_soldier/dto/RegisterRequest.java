package me.dionclei.soviet_soldier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "username is required")
        @Size(min = 4, max = 16, message = "username must be between 4 and 16")
        String username,

        @NotBlank(message = "nickname is required")
        @Size(min = 4, max = 16, message = "nickname must be between 4 and 16")
        String nickname,

        @NotBlank(message = "password is required")
        @Size(min = 6, max = 16, message = "password must be between 6 and 16")
        String password) {
}
