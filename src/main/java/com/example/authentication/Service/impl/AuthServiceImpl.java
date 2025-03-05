package com.example.authentication.Service.impl;

import com.example.authentication.Model.DTO.UserDetailsImpl;
import com.example.authentication.Model.DTO.request.AuthRequest;
import com.example.authentication.Model.DTO.request.LoginRequest;
import com.example.authentication.Model.DTO.request.RegisterRequest;
import com.example.authentication.Model.DTO.response.ApiResponse;
import com.example.authentication.Model.DTO.response.AuthResponse;
import com.example.authentication.Model.Entity.InvalidToken;
import com.example.authentication.Model.Entity.Role;
import com.example.authentication.Model.Entity.User;
import com.example.authentication.Repository.InvalidTokenRepository;
import com.example.authentication.Repository.RoleRepository;
import com.example.authentication.Repository.UserRepository;
import com.example.authentication.Service.AuthService;
import com.example.authentication.exception.AppException;
import com.example.authentication.exception.ErrorCode;
import com.example.authentication.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final InvalidTokenRepository invalidTokenRepository;
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

    @Override
    public ApiResponse<?> refresh(AuthRequest authRequest) {
        // lấy thông tin từ token
        Claims claims = JwtUtils.extractClaims(authRequest.getToken());

        // Tìm xem token đã bị thu hồi chưa
        boolean isInvoked = invalidTokenRepository.existsById(claims.getId());

        // kiểm tra xem tk đã bị xóa
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(claims.getSubject());

        // Nếu tk bị xóa thì sẽ bị ném ngoại lệ ở dòng loaduser
        boolean isValidToken = JwtUtils.isValidToken(authRequest.getToken(), isInvoked, true);

        if(!isValidToken){
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        // tạo token mới
        String access_token = JwtUtils.generateToken(userDetails, JwtUtils.EXPIRATION);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(access_token).refreshToken(authRequest.getToken()).build();

        return ApiResponse.builder()
                .message("refresh token successfully")
                .data(authResponse)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> revoke(AuthRequest authRequest) {
        // thu hồi token
        Claims claims = JwtUtils.extractClaims(authRequest.getToken());
        String id = claims.getId();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(id).expiration(claims.getExpiration()).build();
        invalidTokenRepository.save(invalidToken);

        return ApiResponse.builder()
                .data(invalidToken)
                .message("Revoke token successfully")
                .build();
    }

    @Override
    public ApiResponse<?> logout(AuthRequest authRequest) {
        try{
            // mặc định là khi muốn logout thì access token luôn còn hạn
            Claims access_claims = JwtUtils.extractClaims(authRequest.getToken());
            Claims refresh_claims = JwtUtils.extractClaims(authRequest.getRefresh_token());

            // Chỉ lưu token còn hạn vào
            InvalidToken invalidToken = InvalidToken.builder()
                    .id(refresh_claims.getId()).expiration(refresh_claims.getExpiration()).build();
            invalidTokenRepository.save(invalidToken);

            invalidToken = InvalidToken.builder()
                    .id(access_claims.getId()).expiration(refresh_claims.getExpiration()).build();
            invalidTokenRepository.save(invalidToken);

        }catch (ExpiredJwtException e){
            // refresh token hết hạn thì k lưu
        }

        return ApiResponse.builder()
                .message("logout successfully")
                .build();
    }
}
