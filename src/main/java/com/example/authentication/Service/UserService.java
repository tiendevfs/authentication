package com.example.authentication.Service;

import com.example.authentication.Model.DTO.request.UserRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.UserResponse;

import java.util.List;

public interface UserService {
    ApiResponse<?> findAll();

    ApiResponse<?> create(UserRequest userRequest);

    ApiResponse<?> update(UserRequest userRequest);

    ApiResponse<?> delete(Long id);

    ApiResponse<?> getInfo();
}
