package com.example.authentication.Service.impl;

import com.example.authentication.Model.DTO.UserDetailsImpl;
import com.example.authentication.Model.DTO.request.UserRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.UserResponse;
import com.example.authentication.Model.Entity.Role;
import com.example.authentication.Model.Entity.User;
import com.example.authentication.Model.Mapper.UserMapper;
import com.example.authentication.Repository.RoleRepository;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.Service.RoleService;
import com.example.authentication.Service.UserService;
import com.example.authentication.exception.AppException;
import com.example.authentication.exception.ErrorCode;
import com.example.authentication.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> create(UserRequest userRequest) {
        if(userRepository.existsByUsername(userRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userRequest);
        // mã hóa password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        // lấy role
        List<Role> roles = roleService.findAllByCodes(userRequest.getRole());
        user.setRoles(roles);

        userRepository.save(user);

        return ApiResponse.builder()
                .data(userMapper.toUserResponse(user))
                .message("User created")
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> update(UserRequest userRequest) {
        User user = findUserByUsername(userRequest.getUsername());
        Long id = user.getId();

        user = userMapper.toUser(userRequest);
        // set password
        String password = new BCryptPasswordEncoder().encode(userRequest.getPassword());
        user.setPassword(password);
        user.setId(id);
        // set role
        List<Role> roles = roleService.findAllByCodes(userRequest.getRole());
        user.setRoles(roles);

        userRepository.save(user);

        return ApiResponse.builder()
                .data(userMapper.toUserResponse(user))
                .message("User created")
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> delete(Long id) {
        userRepository.deleteById(id);
        return ApiResponse.builder()
                .message("User deleted").build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getInfo() {
        UserDetailsImpl info = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = findUserByUsername(info.getUsername());

        return ApiResponse.builder()
                .message("Your information")
                .data(userMapper.toUserResponse(user))
                .build();
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
