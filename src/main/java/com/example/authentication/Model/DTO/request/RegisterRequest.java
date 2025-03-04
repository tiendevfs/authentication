package com.example.authentication.Model.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotEmpty(message = "username must not be empty")
    private String username;

    @Size(min = 6, message = "password must be greater than 6 characters")
    private String password;

    private String confirmPassword;
}
