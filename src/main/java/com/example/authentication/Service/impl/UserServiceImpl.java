package com.example.authentication.Service.impl;

import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.UserResponse;
import com.example.authentication.Model.Entity.User;
import com.example.authentication.Model.Mapper.UserMapper;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> data = users.stream().map(user -> userMapper.toUserResponse(user))
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .message("List users")
                .data(data)
                .build();
    }
}
