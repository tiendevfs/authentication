package com.example.authentication.Model.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    @NotEmpty(message = "username must not be empty")
    private String username;

    @Size(min = 6, message = "password must be greater than 6 characters")
    private String password;

    @NotEmpty(message = "fullname must not be empty")
    private String fullname;

    @NotEmpty(message = "phone must not be empty")
    private String phone;

    @Min(value = 16)
    private int age;

    private List<String> role;
}
