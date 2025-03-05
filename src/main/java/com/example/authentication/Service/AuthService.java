package com.example.authentication.Service;

import com.example.authentication.Model.DTO.request.AuthRequest;
import com.example.authentication.Model.DTO.request.LoginRequest;
import com.example.authentication.Model.DTO.request.RegisterRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.AuthResponse;

public interface AuthService {
    ApiResponse<?> login(LoginRequest request);

    ApiResponse<?> register(RegisterRequest request);

    ApiResponse<?> refresh(AuthRequest authRequest);

    ApiResponse<?> logout(AuthRequest authRequest);
    ApiResponse<?> revoke(AuthRequest authRequest);
}
