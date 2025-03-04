package com.example.authentication.Service;

import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.UserResponse;

import java.util.List;

public interface UserService {
    ApiResponse<?> findAll();
}
