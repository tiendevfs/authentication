package com.example.authentication.Service.impl;

import com.example.authentication.Model.DTO.UserDetailsImpl;
import com.example.authentication.Model.DTO.request.LoginRequest;
import com.example.authentication.Model.DTO.request.RegisterRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.AuthResponse;
import com.example.authentication.Model.Entity.Role;
import com.example.authentication.Model.Entity.User;
import com.example.authentication.Repository.RoleRepository;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.Service.AuthService;
import com.example.authentication.exception.AppException;
import com.example.authentication.exception.ErrorCode;
import com.example.authentication.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public ApiResponse<?> login(LoginRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getUsername());

        if(!new BCryptPasswordEncoder().matches(request.getPassword(), userDetails.getPassword())){
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }
        String access_token = JwtUtils.generateToken(userDetails, JwtUtils.EXPIRATION);
        String refresh_token = JwtUtils.generateToken(userDetails, JwtUtils.REFRESHABLE);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build();
        return ApiResponse.builder()
                .message("Login successfully")
                .data(authResponse)
                .build();
    }

    @Override
    public ApiResponse<?> register(RegisterRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        // kiểm tra username đã tồn tại
        if(user.isPresent()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        // tạo role user
        Role role = roleRepository.findByCode("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User creUser = User.builder()
                .username(request.getUsername())
                .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                .roles(List.of(role))
                .build();
        userRepository.save(creUser);

        return ApiResponse.builder()
                .data(creUser)
                .message("Register Successfully")
                .build();
    }
}
