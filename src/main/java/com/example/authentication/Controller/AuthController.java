package com.example.authentication.Controller;

import com.example.authentication.Model.DTO.UserDetailsImpl;
import com.example.authentication.Model.DTO.request.LoginRequest;
import com.example.authentication.Model.DTO.request.RegisterRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.Entity.User;
import com.example.authentication.Service.AuthService;
import com.example.authentication.exception.AppException;
import com.example.authentication.exception.ErrorCode;
import com.example.authentication.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @PostMapping("/register")
    public ApiResponse<?> getClaims(@RequestBody @Valid RegisterRequest request){
        return authService.register(request);
    }


}
